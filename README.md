---

# Описание использования модуля

> [1. Установка](#installation)

> [2. Установить имя проекта](#set-project-name)

> [3. Добавить сканирование пакетов модуля Spring'ом](#add-component-scan)

> [4. Расширить класс BaseControllerAdvice](#extends-advice)

> [5. Расширить аспекты](#extends-aspects)

> [6. Возвращаемый тип контроллеров](#response-type)

> [7. Выгрузка в csv или excel](#excel-csv)

---

## 1. Установка

Добавить проект base-web как модуль в новый проект микросервиса (git clone в корневом каталоге) или как зависимость в build.gadle.

---

## 2. Установить имя проекта

В файле application.properties задать свойство app.name= <Имя проекта>

---

## 3. Добавить сканирование пакетов модуля Spring'ом

Добавить в класс Application (main class) в аннотацию @ComponentScan корневой пакет модуля base-web.

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

В проекте микросервиса расширикь классы ...aspect.BaseControllerAspect и ...aspect.BaseControllerExceptionAspect, 
переопределив функции-pointcut'ы controllersPackagePointcut и controllersAdvicePackagePointcut и указав соответствующие пакеты контроллеров 
например "execution(* ru.example.controller.*.*(..))" и "execution(* ru.example.controller.advice.*.*(..))" соответственно.

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