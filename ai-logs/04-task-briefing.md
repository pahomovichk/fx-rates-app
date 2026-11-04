# Currency details task briefing

Exported from the Cursor chat. Assistant replies are included in full.
Tool calls are listed by name and a short description; file contents passed into tools are omitted because the resulting code is in git.

## 1. User

_Wednesday, Nov 4, 2026, 1:30 PM (UTC+1)_

Мне в рамках тестового задания поставлена задача: 
[
Это приложение на Kotlin Multiplatform + Compose Multiplatform (Decompose, MVIKotlin, kotlin-inject, Ktor) со списком курсов валют. Данные берутся из API Frankfurter, ключ не нужен. Нужны JDK 21, Android SDK 37 и актуальная Android Studio (AGP 9.5).
Правила проекта лежат в AGENTS.md и docs/. Они обязательны: мы оцениваем, насколько итоговый код им соответствует.
Задача: экран валюты
По нажатию на валюту в списке открывается экран, на котором есть:
текущий курс к базовой валюте;
курсы за последние 30 дней (можно списком, график по желанию);
изменение за период в абсолютном значении и в процентах, со знаком;
состояния загрузки и ошибки с кнопкой повтора;
возврат назад к списку.
Логику экрана покройте unit-тестами.
] 
В рамках контекста проекта рассмотри требования к задаче. Задай мне несколько вопросов касательно бизнес-реализации и возможных edge-cases этой фичи. На основе изначальной постановки и ответов на вопросы составь md файл - спеку к данной фиче в папке /task-currency-details.

### Assistant

Сначала разберу, как устроены экран списка, навигация и данные курсов, чтобы вопросы были про реальные развилки этой фичи, а не общие.

Tools:

- Glob: docs/**/*.md
- Glob: shared/src/commonMain/kotlin/**/*.kt
- Glob: **/*Test*.kt


Tools:

- Read: ~/work/test/fx-rates-app/docs/architecture.md
- Read: ~/work/test/fx-rates-app/docs/screen.md
- Read: ~/work/test/fx-rates-app/docs/conventions.md
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/data/remote/FrankfurterApi.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/domain/repository/RatesRepository.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/data/remote/dto/TimeSeriesDto.kt


Tools:

- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/domain/model/CurrencyRate.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/core/money/RateChange.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/data/repository/RatesRepositoryImpl.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/data/repository/RatesConverter.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/root/RootUiComponent.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/mvi/RatesListStore.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/RatesListUiState.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/model/RateItem.kt


Tools:

- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/mvi/RatesListExecutor.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/RatesListUiStateConverter.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/core/format/NumberFormatter.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/RatesListUiComponent.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/ui/Strings.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/core/utils/Resource.kt


Tools:

- Grep: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list
- GetDynamicTools: cursor


Список уже открывает валюту через `OpenCurrency(base, code)`, но экран ещё не собран: в корне навигации колбэк пустой. Курс в приложении — сколько единиц выбранной валюты даёт 1 базовая (сейчас USD). История в API есть только по рабочим дням, а изменение считается тем же `RateChangeCalculator`, что и в списке. Ниже вопросы, от которых зависит спека.

Tools:

- AskQuestion


По загрузке ответ откладываем: сначала сверю, как список реально переключает индикатор, ошибку и повтор, и переспрошу уже по этому поведению.

Tools:

- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/RatesListUiContent.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/mvi/RatesListReducer.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonTest/kotlin/com/fxrates/feature/rates/list/mvi/RatesListStoreTest.kt


В списке загрузка устроена так: шапка и поиск остаются на экране, а меняется только область под ними. Пока идёт запрос, там индикатор, кнопка **Refresh** в шапке видна, но выключена. Успешный список при повторе не остаётся — его снова сменяет индикатор. Ошибка — текст и кнопка **Retry**; **Refresh** в шапке при этом тоже доступен, и обе кнопки делают один и тот же повтор. Подзаголовок с датой пустой и во время загрузки, и при ошибке. Назад со списка уходить некуда, на экране валюты назад нужен и в этих состояниях.

Tools:

- AskQuestion


Tools:

- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/root/RootUiContent.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonTest/kotlin/com/fxrates/testing/TestData.kt
- Read: ~/work/test/fx-rates-app/docs/commands.md
- Glob: task-currency-details/**


Tools:

- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/di/AppComponent.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonTest/kotlin/com/fxrates/core/money/RateChangeCalculatorTest.kt
- Read: ~/work/test/fx-rates-app/shared/src/commonTest/kotlin/com/fxrates/data/repository/RatesConverterTest.kt


Имя приходит с нажатой строки списка и остаётся в шапке во всех состояниях. Остальное совпадает со схемой списка: шапка на месте, повтор заменяет контент индикатором. Собираю это в спеку.

Tools:

- Write: ~/work/test/fx-rates-app/task-currency-details/spec.md


Спека лежит в `task-currency-details/spec.md`.

Экран открывается с кодом, базой и именем нажатой строки. Имя видно в шапке сразу, в том числе при загрузке и ошибке. Курс тот же, что в списке: сколько единиц валюты даёт 1 базовая. История — календарные 30 дней от сегодняшнего дня в UTC до последней даты API, только вернувшиеся рабочие дни, сверху свежие. Изменение за период считает уже существующий `RateChangeCalculator`: последняя котировка минус самая ранняя. Одна точка или нулевая стартовая котировка оставляют курс без блока изменения. Пустой ответ — отдельное пустое состояние. Сеть и сбой запроса — ошибка с `Retry`. Повтор устроен как у списка: шапка остаётся, а курс на время запроса сменяется индикатором. `Refresh` и `Retry` делают один и тот же запрос. Назад доступен из любого состояния.
