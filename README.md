# Описание использования модуля

> [1. Установка](#1-установка)

> [2. Установить имя проекта](#2-установить-имя-проекта)

> [3. Добавить сканирование пакетов модуля Spring'ом](#3-добавить-сканирование-пакетов-модуля-springом)

> [4. Расширить класс BaseControllerAdvice](#4-расширить-класс-basecontrolleradvice)

> [5. Расширить аспекты](#5-расширить-аспекты)

> [6. Возвращаемый тип контроллеров](#6-возвращаемый-тип-контроллеров)

> [7. Выгрузка в csv или excel](#7-выгрузка-в-csv-или-excel)

> [8. Трассировка](#8-трассировка)

---

## 1. Установка

Добавить base-web как подмодуль в новый проект микросервиса 

```git
git submodule add <.../base-web.git>
```

Подключить использование подмодуля в файле settings.gradle

```gradle
include 'base-web'
```

Добавить подмодуль в dependency проекта gradle.build

```gradle
compile project(':base-web')
```

---

## 2. Установить имя проекта

В файле application.properties задать свойство app.name= <Имя проекта>

---

## 3. Добавить сканирование пакетов модуля Spring'ом

Добавить в класс Application (main class) в аннотацию @ComponentScan корневой пакет модуля base-web.

```java
@ComponentScan({"ru.ithex.baseweb"})
```

---

## 4. Расширить класс BaseControllerAdvice

Создать класс advice (пометить аннотацией @RestControllerAdvice или @ControllerAdvice) в пакете controller.advice, расширяющий класс BaseControllerAdvice. В данный класс добавлять обработку кастомных исключений.

- При добавлении обработчика испольовать следующий шаблон (класс exception приведен в качестве примера):

```java
@ExceptionHandler(InvalidDataAccessResourceUsageException.class)
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public ResponseWrapperDTO baseHandle(InvalidDataAccessResourceUsageException e, HttpServletRequest request) {
    return ResponseWrapperDTO.error(new InternalServerError("Ошибка доступа к данным"));
}
```

Типы ошибок: AuthenticationError, BadRequestError, InternalServerError, ValidationError

---

## 5. Расширить аспекты

- Добавить в dependency проекта AspectJ

```gradle
implementation 'org.aspectj:aspectjrt:1.9.4'
```

- Расширить класс ...aspect.BaseControllerAspect, переопределив функцию-pointcut controllersPackagePointcut

> Пример

```java
@Aspect
@Component
public class ControllerAspect extends BaseControllerAspect {
    public ControllerAspect(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Pointcut("execution(* ru.example.controller.*.*(..))")
    public void controllersPackagePointcut(){}
}
```

- Расширить класс ...aspect.BaseControllerExceptionAspect, переопределив функцию-pointcut controllersAdvicePackagePointcut

> Пример

```java
@Aspect
@Component
public class ControllerExceptionLogger extends BaseControllerExceptionAspect {
    @Pointcut("execution(* ru.example.controller.advice.*.*(..))")
    public void controllersAdvicePackagePointcut(){}
}
```

---

## 6. Возвращаемый тип контроллеров

При реализации контроллеров использовать возвращаемый тип ResponseWrapperDTO.

---

## 7. Выгрузка в csv или excel

Для реализации выгрузки в csv или excel имплементировать интерфейсы ExportCSV<> и ExportExcel<>, где в качестве типа для шаблона указать класс в котором хранятся данные для одной строки. 

> Пример:

```java
public class OkvedExportDTO {
    private final String code;
    private final String description;

    public OkvedExportDTO(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

@Service
public class OkvedExportService implements ExportCSV<OkvedExportDTO>, ExportExcel<OkvedExportDTO> {
    private final String CODE = "Код";
    private final String DESCRIPTION = "Описание";

    private final String[] HEADERS = new String[] {CODE, DESCRIPTION};

    private final String CHARSET = "CP1251";

    private final char DELIMITER = ';';


    @Override
    public String[] getRow(OkvedExportDTO data) {
        return new String[] {data.getCode(), data.getDescription()};
    }

    @Override
    public String[] getHeaders() {
        return HEADERS;
    }

    @Override
    public char getDelimiter() {
        return DELIMITER;
    }

    @Override
    public String getCharsetName() {
        return CHARSET;
    }
}
```

Для выгрузки файлов в byte[] использовать функции printAllToCsv и printAllToExcel соответственно.

> Пример функций контроллера:

```java
@GetMapping(value = "/csv", produces = "text/csv")
public ResponseEntity<byte[]> getCsv(){
    return ResponseEntity.ok()
            .header("Content-Disposition","attachment; filename=\"file.csv\"")
            .body(okvedExportService.printAllToCsv(..));
}

@GetMapping(value = "/excel", produces = "application/vnd.ms-excel")
public ResponseEntity<byte[]> getOkvedsExcel() {
    return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=\"file.xls\"")
            .body(okvedExportService.printAllToExcel(..));
}
```

---

## 8. Трассировка

- Расширить класс ...aspect.BaseExecutionTracingAspect, переопределив функцию-pointcut mainPackagePointcut

> Пример

```java
@Aspect
@Component
public class TracingAspect extends BaseExecutionTracingAspect {
    @Override
    @Pointcut("within(ru.example..*)")
    protected void mainPackagePointcut() {}
}
```

- В файле application.properties задать свойство app.execution.tracing = true
