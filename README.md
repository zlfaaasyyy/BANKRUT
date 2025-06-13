# ATM Simulation Project (BANKRUT - Bank Rakyat Unhas Terpadu) - Kelompok 21

### Deskripsi Singkat
Project ini merupakan sebuah simulasi aplikasi ATM yang dibangun menggunakan bahasa pemrrograman java yang mengimplementasikan prinsip  **Object-Oriented Programming (OOP)** khususnya prinsip **Encapsulation, Abstraction, Inheritance, dan Polymorphism.** Project ini menggunakan struktur
**Model-View-Controller (MVC).** Aplikasi ini menyediakan berbagai fitur-fitur dasar yang umum ditemukan dalam mesin ATM seperti login, pendaftaran, tarik/setor tunai, transfer,dan melihat riwayat transaksi.

---

### Fitur Utama

### A. Halaman Utama
- Pengguna memilih untuk Masuk atau Daftar.
- Pengguna memilih menu daftar apabila belum memiliki akun.
- Pengguna memilih menu masuk apabila sudah memiliki akun.

### B. Halaman Daftar
Mengisi Data diri dan keperluan untuk membuat akun.
   > - Pengguna memasukkan nama lengkap, membuat username dan PIN sebagai syarat untuk membuat akun.
   > - Username dibuat dengan syarat minimal 4 karakter dari huruf maupun gabungan dengan angka dan belum digunakan oleh pengguna lain.
   > - PIN merupakan kombinasi dari 6 kombinasi angka.
   > - Saat berhasil membuat akun, nomor rekening otomatis dibuat oleh proram dengan kombinasi acak 10 angka.

### C. Halaman Login
Memasukkan Username dan PIN yang sudah dibuat sebelumnya di halaman daftar.
   > Pastikan username dan pin yang dimasukkan sesuai dengan yang dibuat sebelumnya

### D. Halaman Dashboard
Setelah login berhasil, akan masuk pada halaman dashboard.
> - Di halaman dashboard terdapat nomor rekening, dan jumlah saldo dari akun tersebut.
> - Terdapat fitur fitur lainnya yang bisa dipilih untuk melakukan transaksi yaitu setor tunai, tarik tunai, transfer, dan riwayat transaksi.

### E. Halaman Setor Tunai dan Tarik Tunai
1. Halaman setor tunai digunakan untuk menambahkan saldo ke dalam rekening.
2. Halaman tarik tunai digunakan untuk mengambil uang tunai dari rekening bank untuk berbagai keperluan. 
> - Memasukkan nominal yang akan disetor atau ditarik.
> -  Terdapat menu pilih nominal cepat.
> -  Nominal yang disetor harus kelipatan 50.000.
> -  Memasukkan PIN untuk mengonfirmasi transaksi.

### F. Halaman Transfer
Halaman ini digunakan untuk mengambil uang tunai dari rekening bank untuk berbagai keperluan. 
> - Memasukkan nomor rekening tujuan
> - Memasukkan nominal yang akan ditransfer
> - Minimal nominal transfer adalah 10.000
> - Memasukkan PIN untuk mengonfirmasi transaksi

### G. Halaman Riwayat Transaksi
Halaman ini digunakan untuk melihat catatan atau history dari semua transaksi yang telah dilakukan, baik itu setor tunai, tarik tunai, maupun transfer. 

### H. Penyimpanan data user dan riwayat transaksi
1. Penyimpanan data user dan riwayat transaksi dengan format JSON yaitu 'users.json' untuk user dan 'transactions' untuk riwayat transaksi
2. Otomatis akan membuat file json `user` dan `transactions` saat aplikasi pertama kali dijalan jika file tersebut belum dibuat.

---

## Teknologi yang Digunakan
- Java (JDK 17 atau kompatibel)
- Gradle (build system)
- VSCode (opsional, tersedia konfigurasi di `.vscode`)
- JSON (untuk penyimpanan data pengguna dan transaksi)

---

## Struktur Proyek
```bash
app/
├── src/
│   └── main/java/project/bankrut/
│       ├── controller/        # Logika aplikasi
│       ├── model/             # Struktur data (user, transaksi)
│       ├── service/           # Akses dan manipulasi data JSON
│       ├── view/              # Antarmuka pengguna berbasis teks
│       └── BankrutApp.java    # Entry point aplikasi
├── resources/                 # (Kosong atau digunakan untuk keperluan tambahan)
├── users.json                 # Data pengguna
├── transactions.json          # Data transaksi
````

---
## Struktur Class
Project ini menggunakan prinsip **Pemrograman Berorientasi Objek (OOP)** dan dibagi dalam beberapa package berikut:

### App
**Main**
- Berfungsi sebagai entry point program.
- Menjalankan `LoginView` sebagai tampilan awal aplikasi.

### Controller
Controller merupakan bagian yang menghubungkan model dan view pada setiap proses request dari user. Isi dari controller:
 > `BaseController`, `LoginController`, `RegisterController`, `DashboardController`, `TarikTunaiController`, `SetorTunaiController`, `TransactionController` dan `RiwayatController`

### Models
Models berfungsi sebagai representasi data dan business logic dalam aplikasi. Models bertugas menyimpan struktur data, mengimplementasikan aturan validasi dan logika, serta menyediakan encapsulation melalui getter/setter untuk melindungi integritas data.  Isi dari Models:
> `BankUser`, `BaseUser`, `Transaction`

### Service
Service bertanggung jawab untuk mengelola transaksi data, validasi bisnis yang rumit, integrasi dengan sistem eksternal seperti file, dan pemrosesan data sebelum dikirim ke model atau view. Isi dari service:
> `JsonBaseService`, `JsonUserDataService`, `TransactionService`, dan `UserDataService`

### View
View berfungsi sebagai presentation layer yang bertanggung jawab menampilkan antarmuka pengguna dan menerima input dari user. 
>  `LoginView`,  `LoginFormView`,  `UserDataService`,  `RegisterView`,  `DashboardView`,  `SetorTunaiView`,  `TarikTunaiView`,  `TransferView`,  `RiwayatView`, dan  `DetailTransaksiView`.

 ---

## Penerapan 4 Pilar OOP
Struktur ini menerapkan 4 pilar OOP, yaitu:
- **Encapsulation**
Menggunakan Access Modifier seperti private, protected , dan public untuk mengontrol akses dan menggunakan method getter untuk akses terkontrol.
- **Inheritance**
Semua controller mewarisi dari  `BaseController` dan harus diimplementasi di subclass.
- **Abstraction**
 `BaseController` digunakan sebagai template tanpa implementasi lengkap dan memaksa subclass mengimplementasikan behavior spesifik.
- **Polymorphism**
Menggunakan method overriding dimana setiap controller mengimplementasikan  `showView()` dan  `setUpEventHandlers()` dengan cara berbeda. Semua controller memiliki method yang sama tapi behavior berbeda.

---

## Cara Menjalankan Aplikasi

1. Pastikan Anda telah menginstall Java JDK atau IDE lain seperti IntelliJ IDEA atau Eclipse).
2. Clone repositori melalui Command Prompt: git clone https://github.com/zlfaaasyyy/BANKRUT.git
3. Atau menggunakan alternatif lain (lebih mudah): Klik tombol code lalu download ZIP, kemudian ekstrak file ZIP ke folder pilihan anda
5. Jalankan project dengan Gradle

```bash
./gradlew build
./gradlew run
```

Atau jika menggunakan Windows:

```cmd
gradlew.bat build
gradlew.bat run
```

---
