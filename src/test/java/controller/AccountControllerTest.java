package controller;

import com.banking.bankingapp.controller.AccountController;
import com.banking.bankingapp.model.dto.request.TransferRequest;
import com.banking.bankingapp.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

@SpringBootTest
@WebMvcTest(value = AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    public void retrieveDetailsForCourse() throws Exception {

        final TransferRequest transferRequest = new TransferRequest();
        transferRequest.setFromAccount("143445387287324702");
        transferRequest.setToAccount("392623174540489902193836");
        transferRequest.setTransferAmount(2000.00);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("transfer", transferRequest).accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
        String expected = "{\n" +
                "    \"creationDate\": \"2023-03-24T11:03:48.164+00:00\",\n" +
                "    \"lastModifiedDate\": \"2023-03-24T11:03:48.164+00:00\",\n" +
                "    \"createdBy\": null,\n" +
                "    \"modifiedBy\": null,\n" +
                "    \"customerId\": 1,\n" +
                "    \"firstName\": \"Mthoko\",\n" +
                "    \"lastName\": \"Sithole\",\n" +
                "    \"gender\": \"Male\",\n" +
                "    \"ethnicity\": \"African\",\n" +
                "    \"email\": \"mondise.mtho@gmail.com\",\n" +
                "    \"bankAccounts\": [\n" +
                "        {\n" +
                "            \"creationDate\": \"2023-03-24T11:03:47.825+00:00\",\n" +
                "            \"lastModifiedDate\": \"2023-03-24T11:03:47.825+00:00\",\n" +
                "            \"createdBy\": null,\n" +
                "            \"modifiedBy\": null,\n" +
                "            \"id\": 1,\n" +
                "            \"accountNumber\": \"143445387287324702\",\n" +
                "            \"balance\": 500,\n" +
                "            \"paymentEnabled\": false,\n" +
                "            \"accountType\": \"SAVINGS\",\n" +
                "            \"customer\": null\n" +
                "        },\n" +
                "        {\n" +
                "            \"creationDate\": \"2023-03-24T11:03:48.131+00:00\",\n" +
                "            \"lastModifiedDate\": \"2023-03-24T11:03:48.131+00:00\",\n" +
                "            \"createdBy\": null,\n" +
                "            \"modifiedBy\": null,\n" +
                "            \"id\": 2,\n" +
                "            \"accountNumber\": \"392623174540489902193836\",\n" +
                "            \"balance\": 0,\n" +
                "            \"paymentEnabled\": true,\n" +
                "            \"accountType\": \"CURRENT\",\n" +
                "            \"customer\": null\n" +
                "        }\n" +
                "    ]\n" +
                "}";


        // {"id":"Course1","name":"Spring","description":"10 Steps, 25 Examples and 10K Students","steps":["Learn Maven","Import Project","First Example","Second Example"]}

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
    }
}
