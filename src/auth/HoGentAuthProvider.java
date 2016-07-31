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
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;
import static util.HashUtil.getSha256;
import static util.JsonUtil.getJson;

public class HoGentAuthProvider implements AuthProvider {

    private String urlBegin = "https://studservice.hogent.be/auth";
    private final GebruikerRepository gebruikerRepository;

    public HoGentAuthProvider(GebruikerRepository gebruikerRepository) {
        this.gebruikerRepository = gebruikerRepository;
    }    
    
    @Override
    public Optional<Gebruiker> authenticate(String userId, String password) {
        if (userId == null || password == null || userId.isEmpty() || password.isEmpty()) 
            return Optional.empty();
        
        String url = String.format("%s/%s/%s", urlBegin, userId, getSha256(password));
        
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
}
