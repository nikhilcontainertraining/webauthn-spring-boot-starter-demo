package com.mih.webauthn.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mih.webauthn.demo.domain.Account;
import com.mih.webauthn.demo.domain.AccountRepo;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

@RestController
public class HomeController {

    @Autowired
    AccountRepo accountRepo;
    @Autowired
    UserDetailsService userDetailsService;

    @GetMapping("/")
    public Map<String, Object> accounts(@AuthenticationPrincipal UserDetails user) {

        RandomString  string = new RandomString();

        try {

            ObjectMapper mapper = new ObjectMapper();

            String filename = user.getUsername() + "_" + string.nextString() + ".json";
            FileOutputStream  fos = new FileOutputStream(filename);
            fos.write(mapper.writeValueAsString(user).getBytes());
            fos.flush();

            System.out.println("FILE_NAME: " + filename);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterable<Account> accounts = accountRepo.findAllByUsername(user.getUsername());
        return Map.of("user", user,
                "accounts", accounts);
    }
}
