package project.bankrut.model;

public abstract class BaseUser {
    protected String namaLengkap;
    protected String username;
    protected String pin;
    protected String nomorRekening;
    protected double saldo;
    
    protected BaseUser(String namaLengkap, String username, String pin) {
        this.namaLengkap = namaLengkap;
        this.username = username;
        this.pin = pin;
        this.saldo = 0.0;
    }
    
    public abstract boolean validasi(String username, String pin);
    public abstract String getInfoAkun();
    
    public String getNamaLengkap() { 
        return namaLengkap; 
    }

    public String getUsername() { 
        return username; 
    }

    public String getNomorRekening() { 
        return nomorRekening; 
    }

    public double getSaldo() { 
        return saldo; 
    }

    public String getPin() { 
        return pin; 
    }
    
    protected void setNomorRekening(String nomorRekening) {
        this.nomorRekening = nomorRekening;
    }
    
    public void setSaldo(double saldo) { 
        this.saldo = saldo; 
    }
    
    public boolean isSaldoCukup(double amount) {
        return saldo >= amount;
    }
}