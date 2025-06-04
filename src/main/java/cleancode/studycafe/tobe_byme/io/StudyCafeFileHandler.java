package cleancode.studycafe.tobe_byme.io;

import cleancode.studycafe.tobe_byme.model.StudyCafeLockerPass;
import cleancode.studycafe.tobe_byme.model.StudyCafePass;
import cleancode.studycafe.tobe_byme.model.StudyCafePassType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class StudyCafeFileHandler {
    private static final String RESOURCE_PATH = "src/main/resources/cleancode/studycafe/";
    private static final String STUDY_CAFE_PASS_LIST_FILE = "pass-list.csv";
    private static final String STUDY_CAFE_LOCKER_FILE = "locker.csv";
    private static final String FILE_NOT_FOUND_ERROR = "파일을 찾을 수 없습니다: ";
    private static final String FILE_READ_ERROR = "파일을 읽는데 실패했습니다: ";
    private static final String INVALID_DATA_FORMAT = "잘못된 데이터 형식입니다: ";
    private static final String INVALID_DATA_LENGTH = "데이터 개수가 잘못되었습니다: ";

    public List<StudyCafePass> readStudyCafePasses() {
        try {
            return readLines(Paths.get(RESOURCE_PATH, STUDY_CAFE_PASS_LIST_FILE))
                .stream()
                .map(this::parseStudyCafePass)
                .toList();
        } catch (IOException e) {
            throw new RuntimeException(FILE_READ_ERROR, e);
        }
    }

    public List<StudyCafeLockerPass> readLockerPasses() {
        try {
            return readLines(Paths.get(RESOURCE_PATH, STUDY_CAFE_LOCKER_FILE))
                .stream()
                .map(this::parseStudyCafeLockerPass)
                .toList();
        } catch (IOException e) {
            throw new RuntimeException(FILE_READ_ERROR, e);
        }
    }

    private List<String> readLines(Path filePath) throws IOException {
        if (filePath == null || !Files.exists(filePath)) {
            throw new IllegalArgumentException(FILE_NOT_FOUND_ERROR + filePath);
        }
        return Files.readAllLines(filePath);
    }

    private StudyCafePass parseStudyCafePass(String line) {
        try {
            String[] values = line.split(",");
            validateValues(values, 4);

            StudyCafePassType studyCafePassType = StudyCafePassType.valueOf(values[0]);
            int duration = Integer.parseInt(values[1]);
            int price = Integer.parseInt(values[2]);
            double discountRate = Double.parseDouble(values[3]);

            return StudyCafePass.of(studyCafePassType, duration, price, discountRate);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(INVALID_DATA_FORMAT + line, e);
        }
    }

    private StudyCafeLockerPass parseStudyCafeLockerPass(String line) {
        try {
            String[] values = line.split(",");
            validateValues(values, 3);

            StudyCafePassType studyCafePassType = StudyCafePassType.valueOf(values[0]);
            int duration = Integer.parseInt(values[1]);
            int price = Integer.parseInt(values[2]);

            return StudyCafeLockerPass.of(studyCafePassType, duration, price);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(INVALID_DATA_FORMAT + line, e);
        }
    }

    private void validateValues(String[] values, int expectedLength) {
        if (values.length != expectedLength) {
            throw new IllegalArgumentException(INVALID_DATA_LENGTH + String.join(",", values));
        }
    }

}
