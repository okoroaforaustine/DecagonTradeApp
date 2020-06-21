/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.decagon.tradeapp.Repository;

import com.decagon.tradeapp.entity.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author austine.okoroafor
 */
@Repository
public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    
    public AppUser findByUsername(String username);
	
	public AppUser findByPassword(String password);
    
}
