package be.xplore.price_scraper.domain.shopping;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@RequiredArgsConstructor
@Entity
public class ItemPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Timestamp timestamp;
    private double price;
    @OneToOne
    private Item item;

}
