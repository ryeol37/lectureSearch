package com.lecturesearch.lecture.OAuth2.domain;

import com.lecturesearch.lecture.OAuth2.SocialType;
import com.lecturesearch.lecture.OAuth2.repository.UserRepository;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Document(indexName = "user", type = "info")
public class User implements UserDetailsService {

    @Id
    private String idx;
    private String name;
    private String email;
    private String password;
    private String principal;
    @Enumerated(EnumType.STRING)
    private SocialType socialType;
    private String createdDate;
    private String lastVisitDate;
    private String status;
    private long numOfVisit;

    @Autowired
    UserRepository userRepository;

    @Builder
    public User(String name, String password,
                String email, String createdDate, String lastVisitDate,
                String principal, SocialType socialType, String status, long numOfVisit){
        this.name=name;
        this.password=password;
        this.email=email;
        this.principal=principal;
        this.socialType=socialType;
        this.createdDate=createdDate;
        this.lastVisitDate=lastVisitDate;
        this.status=status;
        this.numOfVisit=numOfVisit;
    }

    public void setEncodePassword(String password){
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public String getPassword() {
        return this.password;
    }

    public void setCreatedDate(){//facebook, google 에서 제공하는 시간대와 맞춤
        this.createdDate=LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()).toString();
    }
    public void setLastVisitDate() {
        this.lastVisitDate=LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()).toString();
    }
    public void setStatusNormal(){
        this.status="normal";
    }
    public void setStatusBlocked(){
        this.status="blocked";
    }
    public void countVisitNum(){
        this.numOfVisit+=1;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email).get();

        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                return authorities;
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getEmail();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };

        return userDetails;
    }
}
