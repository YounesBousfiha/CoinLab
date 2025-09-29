package application.mapper;

import application.dto.TransactionDTO;
import domain.entity.Transaction;

public class TransactionMapper {


    private TransactionMapper() {}

    public static TransactionDTO toDTO(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setUuid(transaction.getUuid());
        transactionDTO.setSource(transaction.getSource());
        transactionDTO.setDestination(transaction.getDestination());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setFee(transaction.getFee());
        transactionDTO.setPriority(transaction.getPriority());
        transactionDTO.setStatus(transaction.getStatus());
        transactionDTO.setCreatedAt(transaction.getCreatedAt());

        return transactionDTO;
    }

    public static Transaction toEntity(TransactionDTO transactionDTO) {
        return new Transaction.Builder()
                .source(transactionDTO.getSource())
                .uuid(transactionDTO.getUuid())
                .destination(transactionDTO.getDestination())
                .amount(transactionDTO.getAmount())
                .status(transactionDTO.getStatus())
                .fee(transactionDTO.getFee())
                .priority(transactionDTO.getPriority())
                .build();
    }
}
