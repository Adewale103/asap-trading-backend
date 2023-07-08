package com.twinkles.asaptrading.service.user;

import com.twinkles.asaptrading.dto.GenericResponse;
import com.twinkles.asaptrading.dto.UserDto;
import com.twinkles.asaptrading.dto.request.*;
import com.twinkles.asaptrading.entity.AccountDetails;
import com.twinkles.asaptrading.entity.Role;
import com.twinkles.asaptrading.entity.User;
import com.twinkles.asaptrading.enums.AccountDetailsType;
import com.twinkles.asaptrading.exception.AsapTradingException;
import com.twinkles.asaptrading.repository.AccountDetailsRepository;
import com.twinkles.asaptrading.repository.TransactionRepository;
import com.twinkles.asaptrading.repository.UserRepository;
import com.twinkles.asaptrading.security.jwt.TokenProvider;
import com.twinkles.asaptrading.service.transaction.TransactionService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;
    private final AccountDetailsRepository accountDetailsRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;
    private final TransactionService transactionService;


    @Override
    public UserDto createUserAccount(String host, CreateAccountRequest createAccountRequest) throws AsapTradingException {
        validateIfUserExist(createAccountRequest);
        User user = new User(createAccountRequest.getFullName(), createAccountRequest.getEmail(), bCryptPasswordEncoder.encode(createAccountRequest.getPassword()));
        user.setCreatedDate(LocalDateTime.now());
        user.setPhoneNumber(createAccountRequest.getPhoneNumber());

        User savedUser = userRepository.save(user);
        String token = tokenProvider.generateTokenForVerification(String.valueOf(user.getEmail()));
        String verifyPasswordTokenLink = host + "api/v1/auth/verify/"+token;
        VerificationMessageRequest message = VerificationMessageRequest.builder()
                .subject("VERIFY EMAIL")
                .sender("adeyinkawale@gmail.com")
                .body("Hi! Here is the link to verify your AsapTrading account-> " + verifyPasswordTokenLink)
                .receiver(user.getEmail())
                .usersFullName(String.format("%s ", savedUser.getFullName()))
                .build();
        log.info(token);

        return modelMapper.map(savedUser, UserDto.class);
    }

    private void validateIfUserExist(CreateAccountRequest createAccountRequest) throws AsapTradingException {
        User User = userRepository.findUserByEmail(createAccountRequest.getEmail()).orElse(null);
        if (User != null) {
            throw new AsapTradingException("User with the email "+createAccountRequest.getEmail()+" already exist",400);
        }
    }

    @Override
    public UserDto findUserById(String userId) throws AsapTradingException {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(
                        ()-> new AsapTradingException(String.format("User with id %s not found" ,userId), 404));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map((user -> modelMapper.map(user, UserDto.class)))
                .toList();
    }

    @Override
    public UserDto updateUserProfile(String id, UpdateUserDetailsRequest updateRequest) throws AsapTradingException {
        User user = userRepository.findById(Long.valueOf(id)).orElseThrow(
                () -> new AsapTradingException("User id not found", 404)
        );
        User savedUser = modelMapper.map(updateRequest, User.class);
        savedUser.setId(user.getId());
        savedUser.setCreatedDate(user.getCreatedDate());
        userRepository.save(savedUser);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public User findUserByEmail(String email) throws AsapTradingException {
        return userRepository.findUserByEmail(email).orElseThrow(()-> new AsapTradingException("user not found", 400));
    }

    @Override
    public void verifyUser(String token) throws AsapTradingException {
        User user = verifyClaimFrom(token);
        if (user == null){
            throw new AsapTradingException("User id does not exist",404);
        }
        user.setVerified(true);
        userRepository.save(user);
    }

    @Override
    public GenericResponse buyCoin(BuyCoinRequest buyCoinRequest) {
        User user = userRepository.findById(Long.valueOf(buyCoinRequest.getUserId())).orElseThrow(
                () -> new AsapTradingException("User id not found", 404)
        );
      return transactionService.buyCoin(buyCoinRequest,user);
    }

    @Override
    public GenericResponse sellCoin(SellCoinRequest sellCoinRequest) {
        User user = userRepository.findById(Long.valueOf(sellCoinRequest.getUserId())).orElseThrow(
                () -> new AsapTradingException("User id not found", 404)
        );
        if(user.getAccountDetailsList().isEmpty()){
            AccountDetails accountDetails = new AccountDetails();
            accountDetails.setAccountDetailsType(AccountDetailsType.PRIMARY);
            accountDetails.setAccountNumber(sellCoinRequest.getBankAccountNumber());
            accountDetails.setAccountName(sellCoinRequest.getBankAccountName());
            accountDetails.setBankName(sellCoinRequest.getBankName());
            accountDetailsRepository.save(accountDetails);
            user.getAccountDetailsList().add(accountDetails);
            userRepository.save(user);
        }
        return transactionService.sellCoin(sellCoinRequest, user);
    }


    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(id).orElseThrow(() -> new AsapTradingException("user not found", 404));
        org.springframework.security.core.userdetails.User returnedUser = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthorities(user.getRoles()));
        log.info("Returned user --> {}", returnedUser);
        return returnedUser;
    }

    private User verifyClaimFrom(String token) {
        Claims claims = tokenProvider.getAllClaimsFromJWTToken(token);
        Function<Claims, String> getSubjectFromClaim = Claims::getSubject;
        Function<Claims, Date> getExpirationDateFromClaim = Claims::getExpiration;
        Function<Claims, Date> getIssuedAtDateFromClaim = Claims::getIssuedAt;

        String userId = getSubjectFromClaim.apply(claims);
        if (userId == null){
            throw new AsapTradingException("User id not present in verification token", 404);
        }
        Date expiryDate = getExpirationDateFromClaim.apply(claims);
        if (expiryDate == null){
            throw new AsapTradingException("Expiry Date not present in verification token", 404);
        }
        Date issuedAtDate = getIssuedAtDateFromClaim.apply(claims);

        if (issuedAtDate == null){
            throw new AsapTradingException("Issued At date not present in verification token", 404);
        }

        if (expiryDate.compareTo(issuedAtDate) > 14.4 ){
            throw new AsapTradingException("Verification Token has already expired", 404);
        }

        return findUserByEmail(userId);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
        return roles.stream().map(
                role -> new SimpleGrantedAuthority(role.getRoleType().name())
        ).collect(Collectors.toSet());
    }


}
