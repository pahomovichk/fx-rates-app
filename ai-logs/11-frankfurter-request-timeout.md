# Frankfurter request timeout

Exported from the Cursor chat. Assistant replies are included in full.
Tool calls are listed by name and a short description; file contents passed into tools are omitted because the resulting code is in git.

## 1. User

_Wednesday, Nov 4, 2026, 5:25 PM (UTC+1)_

Ktor отдаёт таймаут ошибки в то время как колл апи в браузере проходит успешно 
Ktor: REQUEST https://api.frankfurter.dev/v1/currencies failed with exception: io.ktor.client.network.sockets.ConnectTimeoutException: Connect timeout has expired [url=https://api.frankfurter.dev/v1/currencies, connect_timeout=unknown ms]
2026-11-04 17:05:26.629 32456-32456 System.out              com.fxrates.app                      I  Ktor: REQUEST https://api.frankfurter.dev/v1/2026-10-28..?base=USD failed with exception: io.ktor.client.network.sockets.ConnectTimeoutException: Connect timeout has expired [url=https://api.frankfurter.dev/v1/2026-10-28..?base=USD, connect_timeout=unknown ms]
2026-11-04 17:05:26.632 32456-32456 System.err              com.fxrates.app                      W  io.ktor.client.network.sockets.ConnectTimeoutException: Connect timeout has expired [url=https://api.frankfurter.dev/v1/currencies, connect_timeout=unknown ms]
2026-11-04 17:05:26.632 32456-32456 System.err              com.fxrates.app                      W      at io.ktor.client.plugins.HttpTimeoutKt.ConnectTimeoutException(HttpTimeout.kt:241)
2026-11-04 17:05:26.632 32456-32456 System.err              com.fxrates.app                      W      at io.ktor.client.engine.okhttp.OkUtilsKt.mapOkHttpException(OkUtils.kt:88)
2026-11-04 17:05:26.632 32456-32456 System.err              com.fxrates.app                      W      at io.ktor.client.engine.okhttp.OkUtilsKt.access$mapOkHttpException(OkUtils.kt:1)
2026-11-04 17:05:26.632 32456-32456 System.err              com.fxrates.app                      W      at io.ktor.client.engine.okhttp.OkHttpCallback.onFailure(OkUtils.kt:48)
2026-11-04 17:05:26.632 32456-32456 System.err              com.fxrates.app                      W      at okhttp3.internal.connection.RealCall$AsyncCall.run(RealCall.kt:590)
2026-11-04 17:05:26.632 32456-32456 System.err              com.fxrates.app                      W      at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1100)
2026-11-04 17:05:26.633 32456-32456 System.err              com.fxrates.app                      W      at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
2026-11-04 17:05:26.633 32456-32456 System.err              com.fxrates.app                      W      at java.lang.Thread.run(Thread.java:1571)
2026-11-04 17:05:26.633 32456-32456 System.err              com.fxrates.app                      W      Suppressed: io.ktor.client.network.sockets.ConnectTimeoutException: Connect timeout has expired [url=https://api.frankfurter.dev/v1/2026-10-28..?base=USD, connect_timeout=unknown ms]
2026-11-04 17:05:26.633 32456-32456 System.err              com.fxrates.app                      W          ... 8 more
2026-11-04 17:05:26.633 32456-32456 System.err              com.fxrates.app                      W      Caused by: java.net.SocketTimeoutExc

### Assistant

Ktor падает на connect timeout к Frankfurter, хотя тот же URL в браузере открывается. Сначала посмотрю, как устроен HTTP-клиент и таймауты.

Tools:

- Glob: **/*.{kt,kts,xml}
- Grep: ~/work/test/fx-rates-app


Tools:

- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/data/remote/HttpClientFactory.kt
- Read: ~/work/test/fx-rates-app/shared/src/androidMain/kotlin/com/fxrates/data/remote/HttpClientFactory.android.kt
- Read: ~/work/test/fx-rates-app/androidApp/src/main/AndroidManifest.xml
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/data/remote/FrankfurterApi.kt
- Read: ~/work/test/fx-rates-app/docs/architecture.md
- Grep: ~/work/test/fx-rates-app


Tools:

- Read: ~/work/test/fx-rates-app/shared/build.gradle.kts
- Read: ~/work/test/fx-rates-app/gradle/libs.versions.toml
- Read: ~/work/test/fx-rates-app/docs/conventions.md


Tools:

- Shell: Check Frankfurter DNS and IPv4/IPv6 reachability
- Grep: ~/work/test/fx-rates-app


Tools:

- Shell: Resolve and curl Frankfurter outside sandbox


Tools:

- Shell: Probe emulator connectivity to Frankfurter IPs


Tools:

- Shell: Locate Android SDK adb binary


Tools:

- Shell: Test device DNS and route to Frankfurter


Tools:

- Shell: Test TCP 443 and DNS from the Pixel


Tools:

- Shell: Read app UID and full timeout logcat


Tools:

- Shell: Find the system HTTP proxy on the phone


Tools:

- Shell: Identify dead global proxy and which app set it


Tools:

- Shell: Identify the VPN that owns tun1


Tools:

- Read: ~/work/test/fx-rates-app/androidApp/src/main/kotlin/com/fxrates/app/MainActivity.kt


Tools:

- Shell: Clear dead proxy and relaunch the app


Tools:

- Shell: Check whether the app request succeeded after proxy clear


Tools:

- Grep: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list
- Shell: Capture app screenshot and recent network probes


Tools:

- Read: /tmp/fx-rates-screen.png
- Read: ~/work/test/fx-rates-app/shared/src/commonMain/kotlin/com/fxrates/feature/rates/list/mvi/RatesListExecutor.kt


Ktor здесь ни при чём: на телефоне был включён системный HTTP-прокси `<redacted-proxy>`, и он не отвечает. OkHttp ходит через системный прокси, поэтому запрос к `api.frankfurter.dev` 10 секунд пытался открыть соединение с этим адресом и падал с `ConnectTimeoutException`. В сообщении Ktor `connect_timeout=unknown ms`, потому что плагин `HttpTimeout` не установлен — таймаут берётся из OkHttp (10 секунд).

Браузер на компьютере этот прокси не использует и ходит на API напрямую, поэтому тот же URL открывается.

Прокси сброшен командой `settings put global http_proxy :0`. После перезапуска приложение загрузило курсы: на экране Rates список валют с курсами на 2026-11-04.

Если прокси нужен снова (Charles, mitmproxy и т.п.), его можно вернуть:

```bash
adb shell settings put global http_proxy <redacted-proxy>
```
