# Cuci Sepatu App

Aplikasi desktop berbasis Java Swing untuk manajemen transaksi laundry sepatu. Mendukung autentikasi multi-user (Admin & Staff), pengelolaan transaksi, dan pencatatan status pembayaran.

## Fitur Utama
- **Login & Registrasi**: User dapat mendaftar sebagai Admin atau Staff.
- **Dashboard Admin**: Tambah, cari, hapus, dan ubah status transaksi. Hanya Admin yang dapat menghapus transaksi.
- **Dashboard Staff**: Tambah, cari, dan ubah status transaksi (tanpa akses hapus).
- **Manajemen User**: Admin dapat menghapus user Staff.
- **Pencatatan Transaksi**: Data transaksi mencakup nama pelanggan, jenis sepatu, harga, tanggal, dan status pembayaran.

## Struktur Folder
```
cuci_sepatu_app/
├── src/
│   ├── Main.java                # Entry point aplikasi
│   ├── gui/                     # Frame GUI (Login, Register, Dashboard)
│   ├── model/                   # Model data (User, Admin, Staff, Transaction)
│   ├── dao/                     # Data Access Object (UserDAO, TransactionDAO)
│   └── util/                    # Utility (DBConnection)
├── resources/
│   └── schema.sql               # Skema database MySQL
├── lib/
│   └── mysql-connector-java-8.0.22.jar # Library konektor MySQL
└── bin/                         # (Opsional) Output kompilasi
```

## Database
- Gunakan MySQL.
- Struktur tabel ada di `resources/schema.sql`.
- Default koneksi: `jdbc:mysql://localhost:3306/cucisepatu`, user: `root`, password: (kosong).

## Setup & Menjalankan
1. **Clone repo & install MySQL**
2. **Import skema database**:
   - Jalankan isi `resources/schema.sql` di MySQL.
3. **Kompilasi**:
   - Pastikan `mysql-connector-java-8.0.22.jar` sudah ada di folder `lib/`.
   - Compile semua file Java:
     ```
     javac -cp lib/mysql-connector-java-8.0.22.jar -d bin src/**/*.java
     ```
4. **Jalankan aplikasi**:
   - Jalankan Main:
     ```
     java -cp "bin;lib/mysql-connector-java-8.0.22.jar" Main
     ```
   - (Linux/Mac gunakan `:` sebagai pemisah classpath)

## Catatan
- Role: 1 = Admin, 2 = Staff.
- Password disimpan dalam bentuk plain (untuk demo, sebaiknya gunakan hash di produksi).
- Harga cuci sepatu otomatis berdasarkan jenis sepatu.

## Kontributor
- [Nama Anda]

---
Aplikasi ini dibuat untuk kebutuhan tugas/project manajemen laundry sepatu. 