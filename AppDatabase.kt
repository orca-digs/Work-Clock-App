// Import necessary libraries
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Entity
public class ClockRecord {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private long startTime;
    private long endTime;
    private String note;

    // Getters and Setters
}

@Dao
public interface ClockDao {
    @Insert
    void insert(ClockRecord clockRecord);

    @Query("SELECT * FROM ClockRecord WHERE id = :id")
    ClockRecord getClockRecord(int id);

    @Query("SELECT * FROM ClockRecord")
    List<ClockRecord> getAllClockRecords();
}