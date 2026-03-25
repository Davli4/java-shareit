package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDateDesc(Long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.startDate <= :now AND b.endDate >= :now ORDER BY b.startDate DESC ")
    List<Booking> findCurrentByBooker(@Param("bookerId") Long bookerId,
                                      @Param("now") LocalDateTime now);


    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.endDate < :now ORDER BY b.startDate DESC")
    List<Booking> findPastByBooker(@Param("bookerId") Long bookerId,
                                   @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.startDate > :now ORDER BY b.startDate DESC")
    List<Booking> findFutureByBooker(@Param("bookerId") Long bookerId,
                                     @Param("now") LocalDateTime now);

    List<Booking> findByBookerIdAndStatusOrderByStartDateDesc(Long bookerId, BookingStatus status);


    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId ORDER BY b.startDate DESC")
    List<Booking> findByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.startDate <= :now AND b.endDate >= :now ORDER BY b.startDate DESC")
    List<Booking> findCurrentByOwner(@Param("ownerId") Long ownerId,
                                     @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.endDate < :now ORDER BY b.startDate DESC")
    List<Booking> findPastByOwner(@Param("ownerId") Long ownerId,
                                  @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.startDate > :now ORDER BY b.startDate DESC")
    List<Booking> findFutureByOwner(@Param("ownerId") Long ownerId,
                                    @Param("now") LocalDateTime now);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDateDesc(Long ownerId, BookingStatus status);  // ← status

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.booker.id = :userId AND b.status = :status AND b.endDate < :now")
    List<Booking> findCompletedBookingsByItemAndUser(@Param("itemId") Long itemId,
                                                     @Param("userId") Long userId,
                                                     @Param("status") BookingStatus status,
                                                     @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.status = 'APPROVED' AND b.startDate > :now ORDER BY b.startDate DESC")
    List<Booking> findLastBookingsByItem(@Param("itemId") Long itemId,
                                         @Param("now") LocalDateTime now);


    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.status = 'APPROVED' AND b.startDate > :now ORDER BY b.startDate ASC")
    List<Booking> findNextBookingsByItem(@Param("itemId") Long itemId,
                                        @Param("now") LocalDateTime now);
}