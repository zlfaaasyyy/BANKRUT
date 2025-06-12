Untuk membuat README berdasarkan isi dari file ZIP tersebut (`PROJECT ATM KLP 21.zip`), saya akan mengekstrak dan menganalisis struktur proyek terlebih dahulu. Setelah itu, saya akan buatkan README yang sesuai dengan format standar (seperti untuk GitHub), mencakup:

* Deskripsi proyek
* Fitur utama
* Teknologi yang digunakan
* Cara menjalankan
* Struktur folder
* Kontributor (jika perlu)

Mari saya mulai dengan memeriksa isi file ZIP terlebih dahulu.


Berdasarkan struktur dari file `PROJECT ATM KLP 21.zip`, proyek ini merupakan aplikasi ATM simulasi berbasis Java menggunakan Gradle sebagai sistem build. Berikut adalah file `README.md` yang sesuai:

---

````markdown
# 📟 ATM Simulation Project - Kelompok 21

Proyek ini merupakan simulasi sistem ATM yang dibangun menggunakan bahasa pemrograman Java dengan struktur Model-View-Controller (MVC). Aplikasi ini menyediakan fitur-fitur dasar yang umum ditemukan dalam mesin ATM, seperti login, pendaftaran, tarik/setor tunai, transfer, dan melihat riwayat transaksi.

## 🚀 Fitur Utama

- 🔐 **Login dan Register**
- 💵 **Setor Tunai**
- 💸 **Tarik Tunai**
- 🔁 **Transfer Antar Akun**
- 📄 **Riwayat Transaksi**
- 🧩 **Struktur MVC**
- 🗃️ **Data Tersimpan dalam Format JSON** (`users.json`, `transactions.json`)

## 🛠️ Teknologi yang Digunakan

- Java (JDK 17 atau kompatibel)
- Gradle (build system)
- VSCode (opsional, tersedia konfigurasi di `.vscode`)
- JSON (untuk penyimpanan data pengguna dan transaksi)

## 📁 Struktur Proyek

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

## ▶️ Cara Menjalankan Proyek

1. **Clone atau ekstrak proyek ini**
2. **Buka terminal di direktori `PROJECT ATM KLP 21`**
3. **Build dan jalankan proyek dengan Gradle**:

```bash
./gradlew build
./gradlew run
```

Atau jika menggunakan Windows:

```cmd
gradlew.bat build
gradlew.bat run
```

## 🧪 Pengujian

File test terdapat di:

```
app/src/test/java/project/atm/klp/AppTest.java
```

Jalankan test dengan:

```bash
./gradlew test
```

## 👨‍💻 Kontributor

Kelompok 21 - Proyek Pemrograman Berbasis Objek
**-ANDI FA'ATHIR EKA SAPUTRA A.A**
**-ZALFA SYAUQIYAH HAMKA**
**-NAYLA ZAKY FAUZIAH**
