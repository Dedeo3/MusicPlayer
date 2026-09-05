# Aplikasi Music Player

Aplikasi pemutar musik Android yang simpel, dibangun menggunakan **Kotlin**, mengimplementasikan **Arsitektur MVVM**, serta menerapkan *best practices* dalam pengembangan Android modern.

---

## Fitur
- Melihat daftar lagu
- Memutar, menghentikan sementara (*pause*), dan melanjutkan pemutaran audio
- Navigasi ke lagu berikutnya dan lagu sebelumnya
- *Seekbar* interaktif untuk mengontrol durasi pemutaran audio
- Memutar lagu berikutnya secara otomatis saat lagu selesai

---

## Arsitektur & Tech Stack

Proyek ini menerapkan prinsip **Clean Architecture** berpadu dengan pola **MVVM (Model-View-ViewModel)**.

- **Bahasa:** Kotlin (100%)
- **Arsitektur:** MVVM + Repository Pattern
- **UI:** XML Layouts, ViewBinding
- **Asinkron:** Kotlin Coroutines & Flow
- **Media Engine:** MediaPlayer
- **Dependency Injection:** Koin 
- **Penanganan State:** UI State (Loading, Success, Error)

---

## Pipeline CI/CD & Unduh APK

Proses *build* otomatis dikelola menggunakan **GitHub Actions**.

- **Unduh APK:** Buka tab [Actions](../../actions), pilih workflow run terbaru yang berhasil (centang hijau), lalu unduh APK dari bagian **Artifacts** di bagian bawah halaman.

---

## Pengujian (Testing)

*Unit test* dibuat untuk memverifikasi logika bisnis pada ViewModel dan interaksi pada Repository.

Jalankan *unit test* melalui CLI / Terminal:
```bash
./gradlew test