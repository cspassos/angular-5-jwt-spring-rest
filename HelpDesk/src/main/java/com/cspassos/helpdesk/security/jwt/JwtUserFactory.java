package com.cspassos.helpdesk.security.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.cspassos.helpdesk.entity.User;
import com.cspassos.helpdesk.enums.ProfileEnum;

public class JwtUserFactory {

	private JwtUserFactory() {
	}
	
	//Converte e gera um jwtUser
	public static JwtUser create(User user) {
		return new JwtUser(user.getId(), user.getEmail(), user.getPassword(), 
				mapToGrantedAuthorities(user.getProfile()));
	}

	//Converte o perfil do usuario apra o formato usado pelo spring security
	private static Collection<? extends GrantedAuthority> mapToGrantedAuthorities(ProfileEnum profile) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(profile.toString()));
		return authorities;
	}
}
