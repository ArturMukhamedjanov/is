package islab1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import islab1.auth.services.AuthenticationService;
import islab1.models.DTO.TransactionInfoDTO;
import islab1.models.TransactionInfo;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.models.json.JsonContainer;
import islab1.services.TransactionInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final AuthenticationService authenticationService;
    private final TransactionInfoService transactionInfoService;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<TransactionInfoDTO>> getAllTransactions() {
        User user = authenticationService.getCurrentUser();
        List<TransactionInfo> transactionInfos = new ArrayList<>();
        if (user.getRole() == Role.ADMIN) {
            transactionInfos = transactionInfoService.getAllTransactionInfos();
        } else {
            transactionInfos = transactionInfoService.getTransactionInfosByCreator(user);
        }
        List<TransactionInfoDTO> transactionInfoDTO = transactionInfoService.convertTransactionInfoToDTOs(transactionInfos);
        return ResponseEntity.ok(transactionInfoDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionInfoDTO> getTransactionInfoById(@PathVariable Long id) {
        User user = authenticationService.getCurrentUser();
        Optional<TransactionInfo> transactionInfo = transactionInfoService.getTransactionInfoById(id);
        if (transactionInfo.isEmpty()) {
            return ResponseEntity.status(400).header("ErrMessage", "Transaction with that id does not exist").body(null);
        }
        if (!transactionInfoService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        TransactionInfoDTO transactionInfoDTO = transactionInfoService.convertTransactionInfoToDTOs(List.of(transactionInfo.get())).get(0);
        return ResponseEntity.ok(transactionInfoDTO);
    }

    @PostMapping
    public ResponseEntity<String> executeTransactionScript(@RequestParam("file") MultipartFile file) {
        User user = authenticationService.getCurrentUser();
        JsonContainer jsonContainer;
        TransactionInfo transactionInfo = new TransactionInfo();
        transactionInfo.setCreator(user);
        transactionInfo.setSuccessful(false);
        if (file.isEmpty()) {
            throw new IllegalArgumentException("The uploaded file is empty.");
        }
        try {
            String fileContent = new String(file.getBytes());
            jsonContainer = objectMapper.readValue(fileContent, JsonContainer.class);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error while parsing file: " + e.getMessage());
        }
        Integer result;
        try {
            result = transactionInfoService.executeTransaction(jsonContainer);
            transactionInfo.setSuccessful(true);
            transactionInfo.setAddedObjects(result);
            transactionInfoService.saveTransactionInfo(transactionInfo);
        }catch (Exception e){
            transactionInfoService.saveTransactionInfo(transactionInfo);
            return ResponseEntity.status(400).body("Error while executing transaction: " + e.getMessage());
        }
        return ResponseEntity.ok().body("Transaction executed successfully, total objects added: " + result);
    }
}
