package uz.tenge.transactiontenge.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("humo_log")
public class Humo {
    @Id
    private Long id;
    private String log;
}
