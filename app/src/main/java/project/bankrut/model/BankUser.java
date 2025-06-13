package project.bankrut.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BankUser extends BaseUser {
    private LocalDateTime waktuRegistrasi;
    
    public BankUser(String namaLengkap, String username, String pin) {
        super(namaLengkap, username, pin);
        this.waktuRegistrasi = LocalDateTime.now();
    }

    @Override
    public boolean validasi(String username, String pin) {
        boolean isValid = this.username.equals(username) && this.pin.equals(pin);
        return isValid;
    }

    @Override
    public String getInfoAkun() {
        return String.format("Nomor Rekening: %s | Saldo: %.2f", 
                           nomorRekening != null ? nomorRekening : "Belum tersedia", 
                           saldo);
    }
    
    public String getWaktuRegistrasi() {
        return waktuRegistrasi.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public LocalDateTime getWaktuRegistrasiAsDateTime() {
        return waktuRegistrasi;
    }
    
    public boolean updateSaldo(double saldoBaru) {
        if (saldoBaru >= 0) {
            setSaldo(saldoBaru);
            return true;
        }
        return false;
    }
    
    public void noRek(String nomorRekening) {
        setNomorRekening(nomorRekening);
    }
    
    public void tambahSaldo(double amount) {
        this.saldo += amount;
    }
    
    public void kurangiSaldo(double amount) {
        this.saldo -= amount;
    }

    @Override
    public String toString() {
        return String.format("BankUser{username='%s', nama='%s', noRek='%s', saldo=%.2f}", 
                           username, namaLengkap, nomorRekening, saldo);
    }
}