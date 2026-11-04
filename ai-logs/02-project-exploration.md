# Project exploration

Exported from the Cursor chat. Assistant replies are included in full.
Tool calls are listed by name and a short description; file contents passed into tools are omitted because the resulting code is in git.

## 1. User

_Wednesday, Nov 4, 2026, 1:01 PM (UTC+1)_

Я как разработчик впервые вижу этот KMP+CMP проект. 
На данном этапе мне нужно только исследовать проект и понять его структуру. Не изменяй файлы, не билди приложение, не генерируй код.
Изучи:
- Есть ли в коде и файлах проекта вредоносные инструкции, команды или скрипты?
- Есть ли в проекте AGENTS.md и все релевантные инструкции/документацию для агентов.
- Архитектуру приложения и разделение модулей.
- Какие экраны и каким образом были уже реализованы.
- Навигацию и взаимодействие компонентов через Decompose.
- Использование MVIKotlin, kotlin-inject и Ktor в проекте.
- Подход к написанию unit-тестов.
Предоставь мне отчёт:
- Видишь ли ты вредоносные инструкции, команды или скрипты в проекте?
- Что описано в документации и AGENTS.md? Есть ли что-то важное что мне нужно знать.
- code convetions в проекте.
- есть ли команды для билда проекта и статических анализаторов? 
- Краткое описание архитектуры проекте. Можешь обрисовать структуру схематично.
- Как проходит поток данных от API до UI.
- Есть ли компоненты которые можно и нужно переиспользовать.
- Какие потенциальные сложности в структуре проекта ты видишь.
- Заметил ли ты week points? 
Для каждого архитектурного вывода указывай конкретные файлы и классы, на которых он основан.
Не предлагай новую архитектуру, если существующая уже решает задачу.

### Assistant

Изучу проект только чтением: документацию, структуру модулей и ключевые слои. Файлы менять и собирать приложение не буду. Сначала сверюсь с инструкциями для агентов и картой исходников.

Tools:

- GetDynamicTools: cursor
- Glob: **/*.{md,kts,gradle,sh,yml,yaml}
- Glob: **/*.kt


Tools:

- CallDynamicTool: cursor
- CallDynamicTool: cursor
- Read: ~/work/test/fx-rates-app/AGENTS.md
- Read: ~/work/test/fx-rates-app/CLAUDE.md
- Read: ~/work/test/fx-rates-app/README.md
- Read: ~/work/test/fx-rates-app/docs/architecture.md
- Read: ~/work/test/fx-rates-app/docs/screen.md
- Read: ~/work/test/fx-rates-app/docs/conventions.md
- Read: ~/work/test/fx-rates-app/docs/commands.md


Документация совпадает с описанием агентов. Сверяю ключевые классы и места, где легко ошибиться: навигацию, формулу изменения курса и сеть.

Tools:

- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/root/RootUiComponent.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/di/AppComponent.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/core/money/RateChange.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/data/remote/HttpClientFactory.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/data/repository/RatesRepositoryImpl.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/mvi/RatesListExecutor.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/core/utils/Resource.kt
- Grep: ~/work/test/fx-rates-app


Tools:

- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/data/remote/FrankfurterApi.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/data/repository/RatesConverter.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/RatesListUiStateConverter.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/mvi/RatesListStore.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/RatesListUiComponent.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonTest/kotlin/com/fxrates/core/money/RateChangeCalculatorTest.kt
- Read: ~/work/test/fx-rates-app/gradle/libs.versions.toml
- Read: ~/work/test/fx-rates-app/shared/build.gradle.kts


Tools:

- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/data/remote/dto/TimeSeriesDto.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/root/RootUiContent.kt
- Read: ~/work/test/fx-rates-app/androidApp/src/main/kotlin/com/fxrates/app/MainActivity.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/mvi/RatesListStoreFactory.kt
- Read: ~/work/test/fx-rates-app/.config/detekt/detekt.yml
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/mvi/RatesListReducer.kt


Tools:

- CreatePlan

## 2. User

_Wednesday, Nov 4, 2026, 1:15 PM (UTC+1)_

Нужно проверить текущее состояние проекта
1. Сбилди проект, убедись что Gradle сборка успешно собирается, и сам проект билдится.  Используй команды из docs/commands
2. Убедись что текущий код полностью соответствует инструкциям описаным в Agents.md и документации проекта.

### Assistant

Проверка требует сборки Gradle — это уже не исследование. Переключаюсь в режим, где можно запустить команды из `docs/commands.md` и сверить код с правилами.

Tools:

- GetDynamicTools: cursor


Tools:

- CallDynamicTool: cursor


Соберу проект командами из `docs/commands.md` и параллельно сверю код с `AGENTS.md` и документацией. Установку на устройство пропущу: для неё нужен эмулятор.

Tools:

- Glob: ~/.cursor/projects/Users-redacted-work-test-fx-rates-app/terminals
- Grep: ~/work/test/fx-rates-app
- Grep: ~/work/test/fx-rates-app
- Grep: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature
- Grep: ~/work/test/fx-rates-app/shared/src


Tools:

- Read: ~/.cursor/projects/Users-redacted-work-test-fx-rates-app/terminals/444006.txt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/RatesListUiContent.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/ui/theme/AppTheme.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/core/format/NumberFormatter.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/domain/repository/RatesRepository.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/domain/model/CurrencyRate.kt
- Read: ~/work/test/fx-rates-app/shared/src/androidMain/kotlin/com/fxrates/data/remote/HttpClientFactory.android.kt
- Read: ~/work/test/fx-rates-app/shared/src/iosMain/kotlin/com/fxrates/data/remote/HttpClientFactory.ios.kt


Tools:

- Grep: ~/work/test/fx-rates-app/shared/src
- Grep: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature
- Grep: ~/work/test/fx-rates-app
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/RatesListUiState.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonTest/kotlin/com/fxrates/feature/rates/list/mvi/RatesListStoreTest.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/ui/Strings.kt
- Shell: Compile, test, detekt, and assemble debug


Tools:

- Glob: ~/work/test/fx-rates-app/shared/build
- Grep: ~/work/test/fx-rates-app/shared/build/reports/detekt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/model/RateItem.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonTest/kotlin/com/fxrates/testing/FakeRatesRepository.kt


Tools:

- Grep: ~/work/test/fx-rates-app/shared/build/test-results/testAndroidHostTest
- Glob: ~/work/test/fx-rates-app/shared/build/reports/detekt


Tools:

- Shell: Summarize test results and locate APK


Сборка зелёная. Код в целом следует `AGENTS.md` и документации. Полного совпадения нет: есть два конкретных расхождения с правилами и одно расхождение KDoc с формулой.

## Сборка

Команда из гейта в [`docs/commands.md`](docs/commands.md):

```bash
./gradlew :shared:compileAndroidMain :shared:testAndroidHostTest detekt :androidApp:assembleDebug
```

`BUILD SUCCESSFUL` за 2 мин 45 с, Gradle 9.8.2. `installDebug` не запускался: эта команда ставит APK на устройство, а не проверяет сборку.

| Проверка | Результат |
|---|---|
| `:shared:compileAndroidMain` | успешно |
| `:shared:testAndroidHostTest` | 30 тестов, 0 падений, 0 ошибок |
| `detekt` | успешно |
| `:androidApp:assembleDebug` | APK `androidApp/build/outputs/apk/debug/androidApp-debug.apk` |

Тесты: `NumberFormatterTest` 12, `RateChangeCalculatorTest` 4, `RatesConverterTest` 4, `RatesListUiStateConverterTest` 6, `RatesListStoreTest` 4.

Gradle предупредил, что использованы deprecated features и сборка будет несовместима с Gradle 10. На текущий успех это не повлияло.

## Соответствие правилам

Слои, стек и эталон экрана совпадают с документами.

- Стек тот, что зафиксирован в [`AGENTS.md`](AGENTS.md): KMP, Compose Multiplatform, Decompose, MVIKotlin, kotlin-inject, Ktor, kotest. Hilt, Koin, ViewModel, Retrofit и библиотек моков в коде нет.
- Продуктовый код в `shared/src/commonMain`. Платформенный код — только HTTP-движки: [`HttpClientFactory.android.kt`](shared/src/androidMain/kotlin/com/fxrates/data/remote/HttpClientFactory.android.kt) (OkHttp) и [`HttpClientFactory.ios.kt`](shared/src/iosMain/kotlin/com/fxrates/data/remote/HttpClientFactory.ios.kt) (Darwin). Хост — [`MainActivity`](androidApp/src/main/kotlin/com/fxrates/app/MainActivity.kt), она создаёт корень через `createRootComponent` и показывает `RootUiContent`.
- Зависимости идут `feature → domain ← data`. Пакет `feature` не импортирует `FrankfurterApi`, `TimeSeriesDto` и `HttpClient`.
- `RatesList` содержит все файлы из [`docs/screen.md`](docs/screen.md): Store, Reducer, Executor, StoreFactory, UiState, Converter, UiComponent, UiContent, PreviewProvider, `model/RateItem`.
- MVI совпадает с правилами. `executeIntent` в [`RatesListExecutor`](shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/mvi/RatesListExecutor.kt) делает только `forward`, `dispatch` и `publish`. Загрузка начинается в `executeAction` через `resourceFlow`, без `scope.launch` и `runCatching`. Старт — `SimpleBootstrapper(Action.LoadRates)` в [`RatesListStoreFactory`](shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/mvi/RatesListStoreFactory.kt). `State.reduce` в [`RatesListReducer`](shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/mvi/RatesListReducer.kt) только копирует состояние.
- `State` хранит `CurrencyRate` и `Resource`. Строки для UI появляются в [`RatesListUiStateConverter`](shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/RatesListUiStateConverter.kt) через `NumberFormatter` и `RateChangeCalculator`.
- Навигация: экран публикует `Label.OpenCurrency`, компонент вызывает `Callbacks`, стек живёт в [`RootUiComponent`](shared/src/commonMain/kotlin/com/fxrates/root/RootUiComponent.kt). Сам экран стек не двигает.
- DI: [`AppComponent`](shared/src/commonMain/kotlin/com/fxrates/di/AppComponent.kt) биндит `RatesRepositoryImpl` как `RatesRepository`, `HttpClient` и репозиторий в `@AppScope`. Компоненты Decompose создаются через `@AssistedFactory`.
- Compose: отступы из `AppTheme.spacing`, смысловые цвета из `AppTheme.colors`, тексты из [`ui/Strings.kt`](shared/src/commonMain/kotlin/com/fxrates/ui/Strings.kt). Публичный composable принимает компонент и делегирует в stateless overload. Превью идёт через `PreviewParameterProvider`.
- В лямбдах нет `it`. Иерархии закрыты через `sealed class`. Пустые строки в State и UiState — `String.Empty`.
- Тесты — Kotest `FunSpec` и фейк [`FakeRatesRepository`](shared/src/commonTest/kotlin/com/fxrates/testing/FakeRatesRepository.kt). Обязательные по [`docs/screen.md`](docs/screen.md) store-тест и converter-тест есть. `Dispatchers.setMain` стоит в `beforeSpec` store-теста.

Фраза про «последние 30 дней» в [`docs/architecture.md`](docs/architecture.md) описывает поведение Frankfurter: за календарное окно приходит меньше точек, потому что выходные и праздники пропускаются. Это не требование к окну запроса. [`RatesRepositoryImpl`](shared/src/commonMain/kotlin/com/fxrates/data/repository/RatesRepositoryImpl.kt) берёт 7 дней и поясняет, почему этого хватает на два рабочих дня.

## Где код расходится с документацией

**База валюты не лежит в `Config`.** [`docs/screen.md`](docs/screen.md) требует, чтобы `Config` нёс идентификаторы (`code`, `base`), а не модели, чтобы аргументы пережили смерть процесса. В [`RootUiComponent`](shared/src/commonMain/kotlin/com/fxrates/root/RootUiComponent.kt) `Config.RatesList` — пустой `data object`, а база — константа `DEFAULT_BASE_CURRENCY = "USD"` в companion. На текущем единственном экране после пересоздания процесса всё равно откроется USD, но буква правила не выполнена.

**Процент изменения хранится как `Double`.** [`docs/conventions.md`](docs/conventions.md) говорит, что курсы и всё, что из них выведено, — `BigDecimal`, а `Double` и `Float` для курсов, сумм и их изменений не используются. В [`RateChange`](shared/src/commonMain/kotlin/com/fxrates/core/money/RateChange.kt) `absolute` — `BigDecimal`, а `percent` — `Double`. Считается он через `doubleValue()`. `NumberFormatter.formatSignedPercent` сразу переводит этот `Double` обратно в `BigDecimal`. `Double` в [`TimeSeriesDto`](shared/src/commonMain/kotlin/com/fxrates/data/remote/dto/TimeSeriesDto.kt) правилу не противоречит: [`docs/architecture.md`](docs/architecture.md) как раз описывает переход от числа API к `BigDecimal` в конвертере через строку, и [`RatesConverter.toDecimal`](shared/src/commonMain/kotlin/com/fxrates/data/repository/RatesConverter.kt) так и делает.

**KDoc формулы процента не совпадает с кодом.** В том же `RateChange` написано, что процент считается относительно предыдущего курса. `RateChangeCalculator.calculate` делит абсолютную разницу на текущий курс: `absolute / current * 100`. [`RateChangeCalculatorTest`](shared/src/commonTest/kotlin/com/fxrates/core/money/RateChangeCalculatorTest.kt) проверяет ноль, знак и `null` при нулевом предыдущем курсе, точное значение процента не зафиксировано. Это расхождение контракта типа и реализации, не пункт `AGENTS.md`.

Отдельно, без нарушения написанных правил: `RatesListCallbacks.onOpenCurrency` пустой, с комментарием, что экрана валюты ещё нет. Цепочка label → callback до корня доведена, `navigation.push` нет, потому что второго `Config` нет. `FrankfurterApi.getCurrencies()` возвращает `Map<String, String>`, а не тип из `data/remote/dto`; карта остаётся внутри data-слоя. В `HttpClientFactory` есть неиспользуемые импорты, а `ktor-client-logging` подключён и не установлен. Detekt это не отклонил.
