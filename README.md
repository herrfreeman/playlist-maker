## О проекте Playlist Maker

Playlist Maker - приложение для составления плейлистов и прослушивания сэмплов треков. 
В данном приложении пользователь создает собственные плейлисты, выбирая в качестве обложки изображения с телефона, ищет треки в библиотеке iTunes, добавляет их в избранное и в плейлисты, проигрывает треки. 

![playlist_banner](https://github.com/user-attachments/assets/09a7e585-05cb-4e9a-86e9-5f0c430d17d1)

Данный проект разрабатывался в целях освоения различных технологий, применяющихся в Android разработке.

# Технологии примененные в данном проекте:
- Язык разработки: **Kotlin**
- Шаблон проектирования: **MVVM**, экраны реализованы на **Fragment**, **ViewModel**,  **LiveData**
- **viewBinding**: вспомогательный инструмент для обращения к элементам верстки экрана как к объектам без поиска
- **SingleActivity**: один класс Activity, экраны реализованы на Fragment
- **RecyclerView**: динамические списки с переиспользованием view-элементов
- **Fragment Navigation**: навигация/переключение фрагментов через navController в BottomNavigationView, TabLayoutMediator, findNavController() с использованием графа навигации
- **Glide**: отображение изображений из интернета с модификацией и кешированием
- **Retrofit2**: получение данных из **REST API** iTunes, поиск треков
- **Koin**: разрешение (встраивание) зависимостей в свои классы
- **Kotlin Coroutines**: асинхронное выполнение кода, в основном для работы с интернет и базой данных
- **Room**: объектный интерфейс взаимодействия с локальной базой данных SQLite, используется для хранения истории выбранных треков из поиска, избранных треков, плейлистов
- **Gson**: сериализация/десериализация объектов в json-строки
- **MediaPlayer**: проигрывание музыки из интернет
- **sharedPreferences**: хранение пользовательских настроек
- **PickVisualMedia**: выбор изображений из галереи телефона
- **CollapsingToolbarLayout + NestedScrollView**: сворачиваемые/фиксируемые тулбары
- **BottomSheetBehavior**: вытягивающиеся вспомогательные экраны

