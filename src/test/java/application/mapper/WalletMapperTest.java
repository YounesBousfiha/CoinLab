package application.mapper;



import application.dto.WalletDTO;
import domain.entity.Wallet;
import domain.enums.CryptoType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


class WalletMapperTest {

    @Test
    void testToDTO() {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setAddress("address");
        wallet.setBalance(new BigDecimal("100.50"));
        wallet.setType(CryptoType.BITCOIN);

        WalletDTO dto = WalletMapper.toDTO(wallet);


        assertEquals(1L, dto.getId());
        assertEquals("address", dto.getAddress());
        assertEquals(new BigDecimal("100.50"),dto.getBalance());
        assertEquals(CryptoType.BITCOIN.toString(), dto.getType());
    }


    @Test
    void testToEntity() {
        WalletDTO dto = new WalletDTO();
        dto.setAddress("address");
        dto.setType("BITCOIN");

        Wallet wallet = WalletMapper.toEntity(dto);

        assertEquals(dto.getAddress(), wallet.getAddress());
        assertEquals(CryptoType.BITCOIN, wallet.getType());
    }
}