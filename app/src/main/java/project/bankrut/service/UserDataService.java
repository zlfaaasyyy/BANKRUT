package project.bankrut.service;

import project.bankrut.model.BankUser;
import java.util.List;

public interface UserDataService {
    boolean simpanUser(BankUser user);
    BankUser cariUser(String username);
    boolean userKetemu(String username);
    List<BankUser> getAllUsers();
    boolean updateUser(BankUser user);
}