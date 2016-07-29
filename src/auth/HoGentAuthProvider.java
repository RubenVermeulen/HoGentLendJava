/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auth;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import domein.gebruiker.Gebruiker;
import domein.gebruiker.GebruikerRepository;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.io.IOUtils;

public class HoGentAuthProvider implements AuthProvider {

    private String url = "https://studservice.hogent.be/auth";
    private final GebruikerRepository gebruikerRepository;

    public HoGentAuthProvider(GebruikerRepository gebruikerRepository) {
        this.gebruikerRepository = gebruikerRepository;
    }    
    
    @Override
    public Optional<Gebruiker> authenticate(String userId, String password) {
        if (userId == null || password == null || userId.isEmpty() || password.isEmpty()) 
            return Optional.empty();
        
        url = String.format("%s/%s/%s", url, userId, getSha256(password));
        
        String json = getJson(url);
        
        // Necessary because API returns "[]"
        if (json.equalsIgnoreCase("\"[]\"")) 
            return Optional.empty();
        
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {}.getType();
        Map<String, String> result = gson.fromJson(json, type);
        
        if ( ! result.get("TYPE").equalsIgnoreCase("personeel")) 
            return Optional.empty();
        
        Optional<Gebruiker> user = gebruikerRepository.getBeheerder(result.get("EMAIL"));
        
        if ( ! user.isPresent() ||  (! user.get().isHoofdbeheerder() &&  ! user.get().isBeheerder())) 
            return Optional.empty();
        
        return user;
    }
    
    private String getSha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            
            return DatatypeConverter.printHexBinary(hash);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
    
    private String getJson(String url) {
        try {
            return IOUtils.toString(new URL(url), (Charset) null);
        } catch (IOException ex) {
            return null;
        }
    }
}
