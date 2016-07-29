/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auth;

import domein.gebruiker.Gebruiker;
import java.util.Optional;

public interface AuthProvider {
    
    public Optional<Gebruiker> authenticate(String userId, String password);
    
}
