# Currency details plan

Exported from the Cursor chat. Assistant replies are included in full.
Tool calls are listed by name and a short description; file contents passed into tools are omitted because the resulting code is in git.

## 1. User

_Wednesday, Nov 4, 2026, 2:11 PM (UTC+1)_

Давай составим план выполнения задачи. Для контекста задачи используй task-currency-details/spec.md. При проектировании фичи используй правила описаные в Agents.md и документации проекта. 
В качестве архитектурного референса можешь использовать (привести в пример реализованую фичу)
В плане:
- Укажи конкретные файлы, которые нужно изменить или создать.
- Опиши поток данных от API до UI.
- Опиши необходимые изменения Repository и API.
- Опиши структуру Store и его Intent / Action / Message / State.
- Опиши модель UI-состояния.
- Опиши обработку ошибок и повторную загрузку.
- Опиши стратегию тестирования, составь список тесткейсов. 
Не пиши код и не изменяй файлы.
Проверь, можно ли переиспользовать существующие модели.
Представь последовательность реализации с минимальными зависимостями между этапами.

### Assistant

Составлю план по спецификации и правилам проекта, без изменений в коде. Сначала прочитаю спецификацию, архитектурные документы и эталонный экран.

Tools:

- Glob: ~/work/test/fx-rates-app
- Read: ~/work/test/fx-rates-app/task-currency-details/spec.md


Tools:

- Read: ~/work/test/fx-rates-app/docs/architecture.md
- Read: ~/work/test/fx-rates-app/docs/screen.md
- Read: ~/work/test/fx-rates-app/docs/conventions.md
- Read: ~/work/test/fx-rates-app/docs/commands.md


Tools:

- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/mvi/RatesListStore.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/mvi/RatesListExecutor.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/mvi/RatesListReducer.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/mvi/RatesListStoreFactory.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/domain/repository/RatesRepository.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/data/remote/FrankfurterApi.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/data/remote/dto/TimeSeriesDto.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/data/repository/RatesRepositoryImpl.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/data/repository/RatesConverter.kt


Tools:

- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/RatesListUiState.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/RatesListUiStateConverter.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/RatesListUiComponent.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/root/RootUiComponent.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/root/RootUiContent.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/domain/model/CurrencyRate.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/core/money/RateChange.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/core/utils/Resource.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/ui/Strings.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/di/AppComponent.kt


Tools:

- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/model/RateItem.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/RatesListUiContent.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/core/format/NumberFormatter.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonTest/kotlin/com/fxrates/feature/rates/list/mvi/RatesListStoreTest.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonTest/kotlin/com/fxrates/testing/FakeRatesRepository.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonTest/kotlin/com/fxrates/data/repository/RatesConverterTest.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonTest/kotlin/com/fxrates/feature/rates/list/RatesListUiStateConverterTest.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/ui/theme/AppTheme.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonTest/kotlin/com/fxrates/testing/TestData.kt


Tools:

- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/data/remote/HttpClientFactory.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/RatesListUiStatePreviewProvider.kt
- Grep: ~/work/test/fx-rates-app


Tools:

- GetDynamicTools: cursor


Tools:

- CallDynamicTool: cursor


Экран валюты повторяет слои `RatesList` (`feature/rates/list/`): Store, Reducer, Executor, StoreFactory, UiState, конвертер, компонент, контент и preview. Список уже публикует открытие валюты, корень это намерение игнорирует. Новый экран сам загружает ряд за 30 календарных дней и рисует курс, изменение и историю.

## Что переиспользовать

Подходят существующие типы и функции.

| Что | Как |
|---|---|
| `RatesRepository` | Новый метод на том же интерфейсе. Новый репозиторий не нужен. |
| `FrankfurterApi.getTimeSeries` и `TimeSeriesDto` | Тот же запрос ряда. Добавляется фильтр одной валюты. |
| `RatesConverter` и `Double.toString().toBigDecimal()` | Новый метод конвертера. Перевод числа в `BigDecimal` остаётся тем же. |
| `RateChangeCalculator` | Изменение окна: `calculate(previous = самая ранняя, current = самая поздняя)`. |
| `NumberFormatter` | `formatRate`, `formatSignedRate`, `formatSignedPercent`. |
| `Resource` и `resourceFlow` | Загрузка, успех и ошибка, как у списка. |
| `Strings.REFRESH`, `Strings.RETRY`, `Strings.ratesSubtitle` | Кнопка шапки, кнопка ошибки и подпись `1 {base} · {date}`. |
| `Clock` в `RatesRepositoryImpl` | Начало окна считается тем же часом, в `TimeZone.UTC`. |
| `FakeRatesRepository` | Дополняется ответом истории. Мок-библиотека не появляется. |

`CurrencyRate` для истории не подходит: у него есть код, имя и курс предыдущего рабочего дня. Метод истории возвращает только дату и курс, имени в ответе нет, имя шапки приезжает из строки списка. `RateItem` тоже не подходит: это кликабельная строка списка с кодом, именем и процентом. Строка истории — дата и курс, без подневного изменения. `TimeSeriesDto` остаётся в `data`: экран его не видит.

`Trend` списка остаётся в `feature/rates/list/model`. Экран деталей заводит свой enum с теми же тремя значениями. Иначе `details` начнёт зависеть от UI-модели другого экрана. Правило цвета то же: знак абсолютного изменения, включая ноль после округления форматтера.

Осознанное отличие от примера в `docs/screen.md`: в конфигурацию кроме базы и кода кладётся имя. Это строка, она сериализуется и нужна шапке до ответа API. Сам ряд в конфигурацию не кладётся.

## Файлы

Новые, набор как у `RatesList`:

- `shared/src/commonMain/kotlin/com/fxrates/domain/model/RateQuote.kt` — `date: LocalDate`, `rate: BigDecimal`.
- `shared/src/commonMain/kotlin/com/fxrates/feature/rates/details/mvi/CurrencyDetailsStore.kt`
- `.../mvi/CurrencyDetailsReducer.kt`
- `.../mvi/CurrencyDetailsExecutor.kt`
- `.../mvi/CurrencyDetailsStoreFactory.kt`
- `.../model/HistoryItem.kt` — уже отформатированные дата и курс.
- `.../model/RateChangeItem.kt` — подпись, абсолют, процент, направление.
- `.../model/Trend.kt`
- `.../CurrencyDetailsUiState.kt`
- `.../CurrencyDetailsUiStateConverter.kt`
- `.../CurrencyDetailsUiComponent.kt`
- `.../CurrencyDetailsUiContent.kt`
- `.../CurrencyDetailsUiStatePreviewProvider.kt`
- `shared/src/commonTest/kotlin/com/fxrates/feature/rates/details/mvi/CurrencyDetailsStoreTest.kt`
- `shared/src/commonTest/kotlin/com/fxrates/feature/rates/details/CurrencyDetailsUiStateConverterTest.kt`
- `shared/src/commonTest/kotlin/com/fxrates/data/repository/HistoryWindowStartTest.kt`

Изменить:

- `domain/repository/RatesRepository.kt` — метод истории.
- `data/repository/RatesRepositoryImpl.kt` — окно 30 дней. Семидневный расчёт списка не меняется.
- `data/remote/FrankfurterApi.kt` — необязательный `symbols`.
- `data/repository/RatesConverter.kt` — выборка одной валюты.
- `data/repository/RatesConverterTest.kt` — кейсы выборки.
- `testing/FakeRatesRepository.kt` — ответ и запись вызовов истории.
- `ui/Strings.kt` — `Back`, `Could not load the rate`, `No rates for this period`, `Change · {oldest} – {newest}`.
- `feature/rates/list/mvi/RatesListStore.kt`, `RatesListExecutor.kt`, `RatesListUiComponent.kt`, `RatesListUiContent.kt` — имя в нажатии, лейбле и колбэке.
- `feature/rates/list/mvi/RatesListStoreTest.kt` — лейбл с именем.
- `root/RootUiComponent.kt`, `root/RootUiContent.kt` — конфигурация, push и pop.

`AppComponent` не меняется. `CurrencyDetailsStoreFactory`, конвертер и фабрика компонента помечаются `@Inject` / `@AssistedFactory`, kotlin-inject проводит их через конструктор корня. Привязка `RatesRepositoryImpl` уже есть.

## Поток данных

1. Строка списка несёт код и имя. Нажатие вызывает `onCurrencyClick(code, name)`.
2. `Intent.OnCurrencyClick` публикует `Label.OpenCurrency(base, code, name)`. Имя берётся из строки, база — из состояния списка.
3. Колбэк корня кладёт в стек `Config.Currency(base, code, name)`. Экран стек не трогает.
4. Корень создаёт `CurrencyDetailsUiComponent`. Фабрика стора получает три строки и стартует загрузку через `SimpleBootstrapper`.
5. `Action.LoadHistory(base, code)` вызывает `RatesRepository.getRateHistory`. Реализация считает начало окна, запрашивает ряд этой пары и конвертирует его в `List<RateQuote>` по возрастанию даты.
6. `resourceFlow` сначала даёт `Resource.Loading`, затем `Resource.Data` или `Resource.Error`. Reducer копирует это в `State.quotes`. Аргументы экрана reducer не меняет.
7. `CurrencyDetailsUiStateConverter` превращает состояние в строки, флаги и UI-модели. Текущий курс — последняя котировка, история на экране — от новых к старым, изменение считает `RateChangeCalculator`.
8. Компонент отдаёт `StateFlow<CurrencyDetailsUiState>`. Композабл только рисует и вызывает `onBack` / `onRefresh`.
9. `Back` публикует лейбл, колбэк корня снимает экран. Системная кнопка назад делает то же через `handleBackButton = true`.
10. Повторный заход создаёт новый компонент и новый запрос. Между визитами ряд не хранится.

## Repository и API

`RatesRepository.getRateHistory(base: String, code: String): List<RateQuote>`.

`RatesRepositoryImpl` считает начало так: сегодня в `TimeZone.UTC` минус 30 календарных дней. Конец в API не передаётся. Расчёт вынесен во внутреннюю функцию от `LocalDate`, чтобы тест не ходил в сеть. `getLatestRates` по-прежнему смотрит на 7 дней и по-прежнему запрашивает имена всех валют. История `getCurrencies` не вызывает.

`FrankfurterApi.getTimeSeries` получает необязательный `symbols`. Для истории репозиторий передаёт код выбранной валюты: `GET /v1/{start}..?base={base}&symbols={code}`. Вызов списка параметр не передаёт и получает все валюты, как сейчас. Существующий путь с пустым концом диапазона уже означает «до последней даты API».

`RatesConverter.toQuotes(timeSeries, code)`:

- оставляет дни, где в мапе есть этот код;
- день без кода и валюты, отличные от кода, отбрасывает;
- дату парсит как `LocalDate`, курс переводит через уже существующее строковое представление `Double`;
- сортирует по дате по возрастанию;
- пустой ряд и ряд без этого кода возвращает пустым списком.

## Store

Аргументы создания: `base`, `code`, `name`.

**Intent**

- `OnRefresh` — шапка и кнопка `Retry` вызывают его оба. Отдельного intent для повтора нет, как у списка.
- `OnBack`.

**Action**

- `LoadHistory(base, code)`. Всё нужное для запроса лежит в action. `executeAction` состояние не читает.

**Message**

- `UpdateQuotes(quotes: Resource<List<RateQuote>>)`.

**Label**

- `NavigateBack`. Других лейблов нет: обновить и повторить — это intent, не навигация.

**State**

- `base`, `code`, `name` — строки из конфигурации;
- `quotes: Resource<List<RateQuote>>`, начальное значение `Resource.Loading`.

В состоянии нет строк для экрана, цвета и отсортированной для UI истории. Список в `Resource.Data` остаётся в порядке репозитория: первая котировка — начало окна, последняя — текущий курс.

**Reducer** на `UpdateQuotes` делает `copy(quotes = message.quotes)`.

**Executor**

- `LoadHistory` → `resourceFlow { repository.getRateHistory(base, code) }`, каждое значение уходит в `UpdateQuotes`.
- `OnRefresh` читает базу и код из `state()` и делает `forward(LoadHistory(...))`.
- `OnBack` публикует `NavigateBack`.

**Factory** собирает стор с `SimpleBootstrapper(LoadHistory(base, code))`. Пока запрос идёт, `Refresh` выключен, а `Retry` уже не на экране, поэтому второй запрос с экрана не стартует. Отдельной блокировки в сторе нет — так же устроен список.

## UI-состояние

Шапка живёт вне варианта контента и видна всегда: `code`, `name`, `isRefreshEnabled`. Имя и код берутся из аргументов состояния, в том числе при загрузке, ошибке и пустом окне. `isRefreshEnabled` ложно только при `Resource.Loading`.

`Content` — ровно один вариант:

| Вариант | Источник | Что лежит внутри |
|---|---|---|
| `Loading` | `Resource.Loading` | Ничего. Индикатор. |
| `Error` | `Resource.Error` | Ничего. Текст `Could not load the rate` и кнопка `Retry`. |
| `Empty` | `Resource.Data` и пустой список | Ничего. Текст `No rates for this period`, без `Retry`. |
| `Data` | `Resource.Data` и хотя бы одна котировка | Текущий курс, необязательное изменение, история. |

`Data`:

- подпись курса через `Strings.ratesSubtitle(base, дата последней котировки)`; дата — `LocalDate.toString()`, отдельный форматтер дат не заводится;
- курс через `formatRate`;
- `change: RateChangeItem?`;
- `history: List<HistoryItem>` — тот же ряд, развёрнутый от новых к старым. Строка не кликается.

`RateChangeItem` есть, когда котировок две или больше и `RateChangeCalculator.calculate` вернул значение. Подпись: `Change · {дата первой} – {дата последней}`. Абсолют и процент форматируются существующими методами. Оба числа красятся по одному направлению:

- округлённый абсолют больше нуля — положительный цвет;
- меньше нуля — отрицательный;
- ноль, в том числе когда форматтер даёт `0.0000` / `0.00%` — нейтральный цвет.

Одна котировка или нулевая самая ранняя котировка (`calculate` возвращает `null`) дают курс и историю без блока изменения. Композабл ничего не считает и не форматирует.

## Ошибки и повтор

`resourceFlow` сначала публикует `Loading`, потом результат. Сетевая ошибка, HTTP-ошибка (`expectSuccess`) и ошибка разбора становятся `Resource.Error`. Отмена корутины пробрасывается дальше: уход с экрана отменяет запрос вместе со scope компонента. Пустой список — успешные данные.

Повтор после ошибки: ошибка сменяется индикатором, затем данными или новой ошибкой. Повтор после успеха, который падает: данные сменяются индикатором, затем ошибкой. Предыдущие цифры на экране не остаются, потому что message заменяет весь `Resource`, а не накладывается на старый список. Шапка при этом не пропадает.

`Refresh` включён в ошибке, в пустом окне и на данных. `Retry` виден только в ошибке и запускает тот же `OnRefresh`. `Back` доступен в каждом состоянии, включая загрузку. Успешный пустой ответ остаётся пустым состоянием.

## Тесты

Фейковый репозиторий, живой `Store` из фабрики с `DefaultStoreFactory`, kotest `FunSpec`. Имена описывают поведение.

**`CurrencyDetailsStoreTest`**

- Создание с базой и кодом запрашивает историю этими аргументами и кладёт успех в состояние. Имя в состоянии то, что передали в фабрику.
- Упавший запрос даёт `Resource.Error`. После восстановления фейка `OnRefresh` спрашивает репозиторий ещё раз и кладёт успех.
- Пустой список остаётся `Resource.Data`, а не ошибкой.
- `OnBack` публикует `NavigateBack`.

**`CurrencyDetailsUiStateConverterTest`**

- Несколько котировок: текущий курс — последняя дата, история от новых к старым, абсолют и процент со знаками, подпись изменения с первой и последней датой, цвет по знаку абсолюта. Код и имя на месте, обновление включено.
- Одна котировка: курс и одна строка истории, блока изменения нет.
- Ноль котировок: пустое состояние.
- Самая ранняя котировка нулевая: курса и история есть, блока изменения нет.
- Нулевая разница: блок есть, `0.0000` и `0.00%` без знака, нейтральный цвет.
- Разница, которая округляется форматтером к нулю: тот же нейтральный вид.
- Загрузка: индикатор, код и имя из аргументов, обновление выключено.
- Ошибка: текст ошибки, код и имя на месте, обновление включено.

**`RatesConverterTest`, новые кейсы**

- Из ряда всех валют берётся только запрошенный код.
- День без этого кода пропускается.
- Пустой ряд и ряд без этого кода дают пустой список.
- Даты на выходе по возрастанию, даже если во входной мапе они шли иначе.
- Десятичные разряды сохраняются так же, как в тесте списка (`0.89158` остаётся `0.89158`).

**`HistoryWindowStartTest`**

- Фиксированная дата `2026-11-04` в UTC даёт начало `2026-10-05`.
- Семидневный путь списка этим тестом и этой функцией не затрагивается.

Существующий тест клика списка обновляется: лейбл `OpenCurrency` содержит имя, переданное в intent.

Проверка перед завершением — команда из `docs/commands.md`:

`./gradlew :shared:testAndroidHostTest detekt :androidApp:assembleDebug`

На запущенном приложении: вход в валюту, курс и история, пустое окно, ошибка с `Retry` и `Refresh`, выключенный `Refresh` на время запроса, возврат к списку из загрузки, ошибки, пустого окна и данных.

## Порядок реализации

Этапы 1 и 2 друг от друга не зависят.

1. **Данные.** `RateQuote`, метод репозитория, `symbols` в API, `toQuotes`, функция начала окна, расширение фейка. Сразу тесты конвертера и начала окна. Список и UI ещё не нужны.
2. **Имя в списке.** Intent, лейбл, колбэк и клик строки передают имя. Тест лейбла списка обновляется. Корень по-прежнему может игнорировать колбэк до этапа 5.
3. **Store экрана.** Зависит только от интерфейса из этапа 1. Store, reducer, executor, factory и тест стора. UI ещё нет.
4. **Конвертер UI.** Зависит от формы `State` из этапа 3. UiState, модели строки и изменения, конвертер и его тест. Executor для этого этапа не нужен, его можно дописывать параллельно, как только объявлены `State` и `Message`.
5. **Экран и навигация.** Зависит от колбэка с именем (этап 2) и от `UiState` (этап 4). Компонент, контент, preview, строки, `Config.Currency`, push при открытии, pop при `NavigateBack` и системной кнопке назад, ветка в `RootUiContent`.
6. **Проверка.** Общие тесты, detekt, debug-APK и ручной проход состояний на приложении.

## 2. User

_Wednesday, Nov 4, 2026, 2:45 PM (UTC+1)_

Поправь план с учетом разбиения на этапы: 

API и получение исторических данных. + Domain-модель и расчёт изменений.
Компонент и управление состояниями (MVI).
Навигация.
UI экрана.

### Assistant

Tools:

- Read: ~/work/test/fx-rates-app/shared/src/commonTest/kotlin/com/fxrates/core/money/RateChangeCalculatorTest.kt

## 3. User

_Wednesday, Nov 4, 2026, 2:47 PM (UTC+1)_

Поправь план с учетом разбиения на этапы: 

API и получение исторических данных. + Domain-модель и расчёт изменений.
Компонент и управление состояниями (MVI).
Навигация.
UI экрана.

### Assistant

Перестрою план по четырём этапам. Сначала сверю, что уже покрыто калькулятором изменений, чтобы не завести второй расчёт.

Tools:

- Read: ~/work/test/fx-rates-app/shared/src/commonTest/kotlin/com/fxrates/core/money/RateChangeCalculatorTest.kt
- Read: ~/work/test/fx-rates-app/docs/screen.md


План разбит на четыре этапа. Референс по-прежнему `RatesList`: те же слои и те же правила из `docs/screen.md`. Экран валюты сам загружает ряд и стек не меняет.

## Этап 1. API, исторические данные, доменная модель и расчёт изменений

Цель этапа — получить котировки одной пары за окно. Экрана и стора ещё нет.

**Файлы**

- Создать `shared/src/commonMain/kotlin/com/fxrates/domain/model/RateQuote.kt`.
- Создать `shared/src/commonTest/kotlin/com/fxrates/data/repository/HistoryWindowStartTest.kt`.
- Изменить `domain/repository/RatesRepository.kt`, `data/repository/RatesRepositoryImpl.kt`, `data/remote/FrankfurterApi.kt`, `data/repository/RatesConverter.kt`, `data/repository/RatesConverterTest.kt`, `testing/FakeRatesRepository.kt`.

`CurrencyRate` сюда не подходит: в нём есть код, имя и курс предыдущего рабочего дня. История — только дата и курс. Имени в ответе API нет. `TimeSeriesDto` остаётся в `data`.

**Доменная модель.** `RateQuote`: `date: LocalDate`, `rate: BigDecimal`. База и код в модель не входят, они аргументы метода.

**Repository.** Новый метод того же `RatesRepository`: `getRateHistory(base, code): List<RateQuote>`. Новый репозиторий не заводится.

`RatesRepositoryImpl` считает начало окна тем же `Clock`, что и список: сегодня в `TimeZone.UTC` минус 30 календарных дней. Конец в запрос не передаётся. Расчёт даты вынесен во внутреннюю функцию от `LocalDate`, её проверяет тест без HTTP. Семидневное окно `getLatestRates` не меняется. Имена валют для истории не запрашиваются.

**API.** У `FrankfurterApi.getTimeSeries` появляется необязательный `symbols`. История вызывает уже существующий метод так: `GET /v1/{start}..?base={base}&symbols={code}`. Список параметр не передаёт и по-прежнему получает все валюты. Пустой конец диапазона уже означает «до последней даты API».

**Конвертер.** `RatesConverter.toQuotes(timeSeries, code)`:

- оставляет только дни, где есть запрошенный код;
- пропускает день без этого кода и курсы других валют;
- переводит число через уже существующее `Double.toString().toBigDecimal()`;
- сортирует по дате по возрастанию;
- пустой ряд и ряд без этого кода возвращает пустым списком.

**Расчёт изменений.** Новый калькулятор не пишется. Считает существующий `RateChangeCalculator`. Вход для экрана — уже отсортированный список `RateQuote`: предыдущий курс — первая котировка, текущий — последняя. Ноль или одна котировка изменения не дают. Нулевая первая котировка даёт `null`, это уже покрыто `RateChangeCalculatorTest`. Вызов калькулятора остаётся в конвертере UI на этапе 4: спека относит производные значения туда, в домен изменение не кладётся.

**Тесты этапа**

- Из ряда всех валют берётся только запрошенный код.
- День без этого кода пропускается.
- Пустой ряд и ряд без кода дают пустой список.
- Даты на выходе по возрастанию, даже если во входной мапе порядок другой.
- Дробная часть сохраняется, как в тесте списка: `0.89158` остаётся `0.89158`.
- `2026-11-04` UTC даёт начало окна `2026-10-05`.

## Этап 2. Компонент и управление состояниями

Цель этапа — загрузка, ошибка, пустой ответ и повтор. Зависит только от метода репозитория из этапа 1. Корень и вёрстка не нужны.

**Файлы**

- `feature/rates/details/mvi/CurrencyDetailsStore.kt`
- `feature/rates/details/mvi/CurrencyDetailsReducer.kt`
- `feature/rates/details/mvi/CurrencyDetailsExecutor.kt`
- `feature/rates/details/mvi/CurrencyDetailsStoreFactory.kt`
- `feature/rates/details/CurrencyDetailsUiComponent.kt`
- `feature/rates/details/CurrencyDetailsUiState.kt` — четыре состояния и шапка, без вёрстки.
- `feature/rates/details/CurrencyDetailsUiStateConverter.kt` — на этом этапе выбирает вариант состояния. Оформление чисел и блока изменения дописывается на этапе 4 в том же классе.
- `commonTest/.../feature/rates/details/mvi/CurrencyDetailsStoreTest.kt`

`AppComponent` не меняется: фабрики с `@Inject` и `@AssistedFactory` подхватит kotlin-inject.

**State.** `base`, `code`, `name` и `quotes: Resource<List<RateQuote>>`. Начальное значение — `Resource.Loading`. Список внутри `Resource.Data` остаётся по возрастанию даты. Строк для экрана в сторе нет.

**Intent.** `OnRefresh` и `OnBack`. Повтор из шапки и будущая кнопка `Retry` вызывают один `OnRefresh`.

**Action.** `LoadHistory(base, code)`. В action есть всё для запроса, `executeAction` состояние не читает.

**Message.** `UpdateQuotes(Resource<List<RateQuote>>)`. Reducer делает `copy(quotes = ...)`.

**Label.** `NavigateBack`. Обновление — intent, не навигация.

**Factory** стартует через `SimpleBootstrapper(LoadHistory(base, code))`.

**Executor.** `LoadHistory` оборачивает `repository.getRateHistory` в `resourceFlow` и каждое значение отправляет в `UpdateQuotes`. `OnRefresh` берёт базу и код из `state()` и делает `forward`. `OnBack` публикует `NavigateBack`.

**Компонент.** `CurrencyDetailsUiComponent` отдаёт поток UI-состояния и методы `onRefresh()` и `onBack()`. `Default` создаёт стор, переводит `states` через конвертер и превращает `NavigateBack` в `Callbacks.onBack`. Экран стек не меняет.

Пока запрос идёт, конвертер выключает обновление. Второй запрос с экрана поэтому не стартует. Отдельной блокировки в сторе нет, как у списка.

**Ошибки на этом этапе.** `resourceFlow` сначала даёт `Loading`, затем `Data` или `Error`. Сеть, HTTP и разбор ответа становятся `Resource.Error`. Пустой список остаётся `Resource.Data`. Повтор заменяет весь `Resource`: после ошибки на экране состояния больше нет ошибки, после успеха провал убирает прежние котировки. `CancellationException` пробрасывается, уход с экрана отменяет запрос вместе со scope компонента. `base`, `code` и `name` сообщениями не меняются.

**Тесты стора**

- Создание запрашивает историю с базой и кодом из аргументов и кладёт успех в состояние. Имя остаётся переданным.
- Упавший запрос даёт `Resource.Error`. После восстановления фейка `OnRefresh` спрашивает репозиторий снова и кладёт успех.
- Пустой список остаётся `Resource.Data`.
- `OnBack` публикует `NavigateBack`.

## Этап 3. Навигация

Цель этапа — открыть экран с тремя строками и снять его со стека. Зависит от колбэков компонента. Вёрстка содержимого не нужна.

**Файлы**

- Изменить `feature/rates/list/mvi/RatesListStore.kt`, `RatesListExecutor.kt`, `RatesListUiComponent.kt`, `RatesListUiContent.kt`, `RatesListStoreTest.kt`.
- Изменить `root/RootUiComponent.kt`.
- Ветку в `root/RootUiContent.kt` закрыть здесь вызовом `CurrencyDetailsUiContent`. Публичная функция композабла к этому моменту существует; её тело заполняет этап 4.

**Список.** Нажатие передаёт код и имя строки. `Intent.OnCurrencyClick(code, name)` публикует `Label.OpenCurrency(base, code, name)`. База берётся из состояния списка. Колбэк `onOpenCurrency(base, code, name)` уходит в корень.

**Корень.** В стек кладётся сериализуемая `Config.Currency(base, code, name)`. Ряд котировок в конфигурацию не входит. `Child.CurrencyDetails` создаёт компонент этими тремя строками и колбэком назад. `onBack` и системная кнопка (`handleBackButton = true`) снимают экран. Повторный заход после возврата создаёт новый компонент и новый запрос.

Имя в конфигурации — расширение примера из `docs/screen.md`: это строка, она переживает смерть процесса и нужна шапке до ответа API.

**Тест.** Существующий тест клика списка ожидает лейбл с именем.

## Этап 4. UI экрана

Цель этапа — нарисовать четыре состояния. Зависит от `State` и конвертера этапа 2. Навигацию не меняет.

**Файлы**

- Дописать `CurrencyDetailsUiStateConverter.kt`.
- Создать `feature/rates/details/model/HistoryItem.kt`, `RateChangeItem.kt`, `Trend.kt`.
- Создать `CurrencyDetailsUiContent.kt`, `CurrencyDetailsUiStatePreviewProvider.kt`.
- Создать `commonTest/.../feature/rates/details/CurrencyDetailsUiStateConverterTest.kt`.
- Изменить `ui/Strings.kt`.
- Заполнить тело `CurrencyDetailsUiContent`, которое этап 3 уже подключил в корне.

`Trend` списка не импортируется: это UI-модель другого экрана. У деталей свой enum с теми же тремя значениями, `Up`, `Down`, `Flat`. Цвета берутся из `AppTheme.colors`, отступы из `AppTheme.spacing`.

**Строки.** Новые: `Back`, `Could not load the rate`, `No rates for this period`, `Change · {oldest} – {newest}`. Уже есть и используются как есть: `Refresh`, `Retry`, шаблон `1 {base} · {date}`. Даты — `LocalDate.toString()`, формат `YYYY-MM-DD`. Отдельный форматтер дат не заводится.

**Шапка всегда:** `Back`, код, имя, `Refresh`. Код и имя берутся из аргументов состояния и видны при загрузке, ошибке и пустом окне. `Refresh` выключен только в загрузке. Поиска, смены базы и pull-to-refresh нет.

**Область под шапкой — ровно одно состояние.**

| Состояние | Когда | Что нарисовано |
|---|---|---|
| Загрузка | Первый вход и любой повтор, пока запрос не завершился | Индикатор. Прежних цифр нет. |
| Ошибка | `Resource.Error` | `Could not load the rate` и `Retry`. Кнопка вызывает тот же `onRefresh`. |
| Пусто | Успех и ни одной котировки | `No rates for this period`. Кнопки `Retry` нет. |
| Данные | Хотя бы одна котировка | Текущий курс, при двух и более точках блок изменения, затем история. |

Текущий курс — последняя котировка, через `formatRate`. Подпись — `Strings.ratesSubtitle`. История — тот же ряд от новых к старым: дата и `formatRate`, строка не нажимается.

Блок изменения есть, когда точек две или больше и `RateChangeCalculator.calculate(первая, последняя)` вернул значение. Абсолют — `formatSignedRate`, процент — `formatSignedPercent`. Оба числа красятся по округлённому абсолюту: больше нуля — положительный цвет, меньше нуля — отрицательный, ноль после округления до `0.0000` / `0.00%` — нейтральный, без знака. Одна котировка или `null` от калькулятора оставляют курс и историю без блока.

**Тесты конвертера**

- Несколько котировок: текущий курс с последней датой, история от новых к старым, знаки абсолюта и процента, подпись с первой и последней датой, цвет по абсолюту. Код и имя на месте, обновление включено.
- Одна котировка: курс и одна строка, блока изменения нет.
- Ноль котировок: пустое состояние.
- Самая ранняя котировка нулевая: курс и история есть, блока нет.
- Нулевая разница и разница, которая округляется к нулю: блок есть, числа без знака, нейтральный цвет.
- Загрузка: индикатор, код и имя из аргументов, обновление выключено.
- Ошибка: текст ошибки, код и имя на месте, обновление включено.

Preview покрывает загрузку, ошибку, пустое окно, данные с изменением и данные с одной котировкой.

## Порядок и проверка

Этап 1 ни от чего в этой задаче не зависит. Этап 2 зависит от метода репозитория. Этап 3 зависит от колбэков компонента. Этап 4 зависит от формы `State` и дописывает тот же конвертер. Этапы 3 и 4 сходятся в одной ветке `RootUiContent`.

После этапа 4 зелёная команда из `docs/commands.md`:

`./gradlew :shared:testAndroidHostTest detekt :androidApp:assembleDebug`

На запущенном приложении проверяются вход в валюту, курс и история, пустое окно, ошибка с `Retry` и `Refresh`, выключенный `Refresh` на время запроса и возврат к списку из загрузки, ошибки, пустого окна и данных.

## 4. User

_Wednesday, Nov 4, 2026, 2:51 PM (UTC+1)_

запиши текущий общий план выполнения работ в task-currency-details/plan

### Assistant

Запишу общий план в `task-currency-details`. Сначала посмотрю, как там лежит спецификация, чтобы файл встал рядом в том же виде.

Tools:

- Glob: ~/work/test/fx-rates-app


Спека лежит как `spec.md`. Рядом запишу общий план четырёх этапов в `plan.md`.

Tools:

- Write: ~/work/test/fx-rates-app/task-currency-details/plan.md


Общий план записан в `task-currency-details/plan.md`, рядом со `spec.md`.

В файле четыре этапа: данные и расчёт изменений, компонент и MVI, навигация, UI экрана. Для каждого этапа указаны файлы, поток данных и тесты, в конце — команда проверки.

## 5. User

_Wednesday, Nov 4, 2026, 2:56 PM (UTC+1)_

сформируй план для каждого этапа в отдельности со следующими пунктами: 
Требования, необходимые тесты, Порядок работы

### Assistant

Tools:

- Read: ~/work/test/fx-rates-app/task-currency-details/plan.md


Разобью общий план на четыре отдельных документа: у каждого будут требования, тесты и порядок работы. В `plan.md` добавлю ссылки на них.

Tools:

- Write: ~/work/test/fx-rates-app/task-currency-details/stage-1.md
- Write: ~/work/test/fx-rates-app/task-currency-details/stage-2.md
- Write: ~/work/test/fx-rates-app/task-currency-details/stage-3.md
- Write: ~/work/test/fx-rates-app/task-currency-details/stage-4.md
- StrReplace: ~/work/test/fx-rates-app/task-currency-details/plan.md


Для каждого этапа лежит отдельный план. В `task-currency-details/plan.md` на них есть ссылки.

| Этап | Файл |
|---|---|
| API, исторические данные, доменная модель и расчёт изменений | `task-currency-details/stage-1.md` |
| Компонент и управление состояниями | `task-currency-details/stage-2.md` |
| Навигация | `task-currency-details/stage-3.md` |
| UI экрана | `task-currency-details/stage-4.md` |

В каждом файле три раздела.

**Требования** фиксируют поведение этапа и список файлов. На этапе 2 конвертер только выбирает загрузку, ошибку, пустое окно и данные, а поля данных оставляет пустыми. На этапе 4 та же ветка заполняется курсом, изменением и историей.

**Необходимые тесты** относятся только к этому этапу. У данных это конвертер ряда и начало окна, у MVI — стор, у навигации — обновлённый клик списка, у UI — конвертер состояния и ручная проверка на приложении.

**Порядок работы** идёт так, чтобы следующий шаг опирался на предыдущий и заканчивался прогоном тестов. Полная команда со сборкой APK стоит в конце этапа 4.
