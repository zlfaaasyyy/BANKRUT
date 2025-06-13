package project.bankrut.service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import project.bankrut.model.BankUser;
import java.io.IOException;
import java.util.*;

public class JsonUserDataService extends JsonBaseService implements UserDataService {
    private static final String USER_DATA_FILE = "users.json";
    private final Random random;
    
    public JsonUserDataService() {
        super(USER_DATA_FILE);
        this.random = new Random();
    }
    
    private String generateNomorRekening() {
        Set<String> existingNumbers = getExistingAccountNumbers();
        String nomorRekening;
        
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                sb.append(random.nextInt(10));
            }
            nomorRekening = sb.toString();
        } while (existingNumbers.contains(nomorRekening));
        
        return nomorRekening;
    }
    
    private Set<String> getExistingAccountNumbers() {
        Set<String> accountNumbers = new HashSet<>();
        ArrayNode usersArray = loadArray();
        
        for (int i = 0; i < usersArray.size(); i++) {
            ObjectNode user = (ObjectNode) usersArray.get(i);
            if (user.has("nomorRekening")) {
                accountNumbers.add(user.get("nomorRekening").asText());
            }
        }
        
        return accountNumbers;
    }

    private ObjectNode userToNode(BankUser user) {
        ObjectNode userNode = objectMapper.createObjectNode();
        userNode.put("namaLengkap", user.getNamaLengkap());
        userNode.put("username", user.getUsername());
        userNode.put("pin", user.getPin());
        userNode.put("nomorRekening", user.getNomorRekening());
        userNode.put("saldo", user.getSaldo());
        userNode.put("registrationDate", user.getWaktuRegistrasi());
        return userNode;
    }
    
    private BankUser nodeToUser(ObjectNode userNode) {
        BankUser user = new BankUser(
            userNode.get("namaLengkap").asText(),
            userNode.get("username").asText(),
            userNode.get("pin").asText()
        );
        user.noRek(userNode.get("nomorRekening").asText());
        user.updateSaldo(userNode.get("saldo").asDouble());
        return user;
    }
    
    @Override
    public boolean simpanUser(BankUser user) {
        try {
            ArrayNode usersArray = loadArray();
            
            if (userKetemu(user.getUsername())) {
                System.out.println("Username sudah digunakan: " + user.getUsername());
                return false;
            }

            if (user.getNomorRekening() == null || user.getNomorRekening().isEmpty()) {
                String accountNumber = generateNomorRekening();
                user.noRek(accountNumber);
            }

            usersArray.add(userToNode(user));
            saveArray(usersArray);
            
            System.out.println("User berhasil disimpan: " + user.getUsername() + 
                             " dengan nomor rekening: " + user.getNomorRekening());
            return true;
            
        } catch (IOException e) {
            System.err.println("Error menyimpan user: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public BankUser cariUser(String username) {
        ArrayNode usersArray = loadArray();
        
        for (int i = 0; i < usersArray.size(); i++) {
            ObjectNode userNode = (ObjectNode) usersArray.get(i);
            if (userNode.get("username").asText().equals(username)) {
                BankUser user = nodeToUser(userNode);
                System.out.println("User ditemukan: " + username + 
                                 " dengan saldo: " + user.getSaldo());
                return user;
            }
        }
        
        System.out.println("User tidak ditemukan: " + username);
        return null;
    }
    
    @Override
    public boolean userKetemu(String username) {
        return cariUser(username) != null;
    }
    
    @Override
    public List<BankUser> getAllUsers() {
        List<BankUser> users = new ArrayList<>();
        ArrayNode usersArray = loadArray();
        
        for (int i = 0; i < usersArray.size(); i++) {
            ObjectNode userNode = (ObjectNode) usersArray.get(i);
            users.add(nodeToUser(userNode));
        }
        
        return users;
    }
    
    @Override
    public boolean updateUser(BankUser user) {
        try {
            ArrayNode usersArray = loadArray();
            
            for (int i = 0; i < usersArray.size(); i++) {
                ObjectNode userNode = (ObjectNode) usersArray.get(i);
                if (userNode.get("username").asText().equals(user.getUsername())) {
                    userNode.put("saldo", user.getSaldo());
                    saveArray(usersArray);
                    System.out.println("User berhasil diupdate: " + user.getUsername() + 
                                     " saldo baru: " + user.getSaldo());
                    return true;
                }
            }
            System.out.println("User tidak ditemukan untuk update: " + user.getUsername());
            return false;
        } catch (IOException e) {
            System.err.println("Error update user: " + e.getMessage());
            return false;
        }
    }

    public boolean validateUser(String username, String pin) {
        BankUser user = cariUser(username);
        return user != null && user.validasi(username, pin);
    }
}
