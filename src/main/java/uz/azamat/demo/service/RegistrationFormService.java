package uz.azamat.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.azamat.demo.dao.RegistrationFormDao;
import uz.azamat.demo.model.IncomingDocuments;
import uz.azamat.demo.model.IncomingDocumentsUI;
import uz.azamat.demo.model.RegistrationForm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;

@Service
public class RegistrationFormService {

    @Autowired
    private RegistrationFormDao registrationFormDao;

    @Value("${files.folder}")
    private String filesFolderPath;

    public IncomingDocumentsUI getById(int id) {
        return registrationFormDao.getById(id);
    }

    public List<IncomingDocuments> getAllData() {
        return registrationFormDao.getAllData();
    }

    public List<IncomingDocuments> getAllFromCentralBankViaEmailForCurrentMonth() {
        return registrationFormDao.getAllFromCentralBankViaEmailForCurrentMonth();
    }

    public List<IncomingDocuments> getAllForFirstQuarterOfThisYearExceptFromGniViaCurrier() {
        return registrationFormDao.getAllForFirstQuarterOfThisYearExceptFromGniViaCurrier();
    }

    public List<IncomingDocuments> getAllFromTsjInCurrentMonthExceptCredits() {
        return registrationFormDao.getAllFromTsjInCurrentMonthExceptCredits();
    }

    public void saveAllData(RegistrationForm registrationForm) throws IOException {

        IncomingDocuments doc = new IncomingDocuments();
        doc.setId(registrationForm.getId());

        String registerNumber = registrationForm.getRegisterNumber();
        doc.setRegisterNumber(registerNumber);

        String registerDate = registrationForm.getRegisterDate();
        Date date = Date.valueOf(registerDate);
        doc.setRegisterDate(date);

        String leaveRegisterNumber = registrationForm.getLeaveRegisterNumber();
        if (leaveRegisterNumber != null && !leaveRegisterNumber.isEmpty()) {
            doc.setOutgoingNumber(leaveRegisterNumber);
        }

        String leaveDate = registrationForm.getLeaveDate();
        if (!leaveDate.isEmpty()) {
            Date date2 = Date.valueOf(leaveDate);
            doc.setOutgoingDate(date2);
        }

        String formDelivery = registrationForm.getFormDelivery();
        doc.setDeliveryType(Integer.parseInt(formDelivery));

        String correspondent = registrationForm.getCorrespondent();
        doc.setCorrespondent(Integer.parseInt(correspondent));

        String topic = registrationForm.getTopic();
        doc.setTheme(topic);

        String description = registrationForm.getDescription();
        doc.setDescription(description);

        String executionPeriod = registrationForm.getExecutionPeriod();
        if (!executionPeriod.isEmpty()) {
            Date date3 = Date.valueOf(executionPeriod);
            doc.setDueDate(date3);
        }

        int access = registrationForm.getAccess();
        doc.setAccess(access);

        int control = registrationForm.getControl();
        doc.setControl(control);

        MultipartFile file = registrationForm.getFile();
        byte[] bytes = file.getBytes();
        String fileName = System.currentTimeMillis() + file.getOriginalFilename();
        Path path = Paths.get(filesFolderPath + fileName);
        Files.write(path, bytes);
        doc.setFileName(file.getOriginalFilename());

        doc.setFilePathName(fileName);

        registrationFormDao.save(doc);
    }

    public IncomingDocuments getByRegistrationNumber(String registerNumber) {
        return registrationFormDao.getByRegNum(registerNumber);
    }

    public boolean isDuplicateRegistrationNumber(String registerNumber) {
        return registrationFormDao.getByRegNum(registerNumber) != null;
    }
}