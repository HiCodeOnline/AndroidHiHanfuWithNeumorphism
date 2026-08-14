package online.hicode.android.hihanfu.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import online.hicode.android.hihanfu.data.entity.Hanfu
import online.hicode.android.hihanfu.data.entity.HanfuCabinet
import online.hicode.android.hihanfu.data.entity.HanfuImage
import online.hicode.android.hihanfu.data.entity.Jewelry
import online.hicode.android.hihanfu.data.entity.JewelryCabinet
import online.hicode.android.hihanfu.data.entity.JewelryImage
import online.hicode.android.hihanfu.data.entity.Outfit
import online.hicode.android.hihanfu.data.entity.OutfitHanfuRel
import online.hicode.android.hihanfu.data.entity.OutfitImage
import online.hicode.android.hihanfu.data.vo.StatisticsVO

/**
 *
 * @author HiCode.Online
 * @since 2026-07-22
 */
@Dao
interface HanfuDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHanfu(hanfu: Hanfu) : Long

    @Update
    suspend fun updateHanfu(hanfu: Hanfu)

    @Query("DELETE FROM hanfu WHERE id = :id")
    suspend fun deleteHanfuById(id: Long)

    @Query("""
        SELECT * FROM hanfu
        WHERE cabinetId = :cabinetId
        ORDER BY id DESC
    """)
    fun listHanfu(cabinetId: Long): Flow<List<Hanfu>>


    //**********  hanfu cabinet  **********//
    @Insert
    suspend fun insertHanfuCabinet(hanfuCabinet: HanfuCabinet)

    @Update
    suspend fun updateHanfuCabinet(hanfuCabinet: HanfuCabinet)

    @Query("DELETE FROM hanfu_cabinet WHERE id = :id")
    suspend fun deleteHanfuCabinetById(id: Long)

    @Query("""
        SELECT * FROM hanfu_cabinet
        ORDER BY id DESC
    """)
    fun listHanfuCabinet(): Flow<List<HanfuCabinet>>

    @Query("""
        SELECT cabinetId, COUNT(id) as count
        FROM hanfu
        GROUP BY cabinetId
    """)
    fun countHanfuCabinet(): Flow<List<StatisticsVO>>

    @Query("""
        SELECT * FROM hanfu_image
        WHERE hanfuId IN ( SELECT id FROM hanfu WHERE cabinetId = :cabinetId )
        ORDER BY id DESC
    """)
    fun listHanfuImageByCabinet(cabinetId: Long): List<HanfuImage>

    @Query("DELETE FROM hanfu_image WHERE hanfuId IN (SELECT id FROM hanfu WHERE cabinetId = :cabinetId)")
    fun deleteHanfuImageByCabinet(cabinetId: Long)

    @Query("DELETE FROM hanfu WHERE cabinetId = :cabinetId")
    fun deleteHanfuByCabinet(cabinetId: Long)

    @Query("""
        SELECT * FROM hanfu_image
        WHERE hanfuId = :hanfuId
        ORDER BY id DESC
    """)
    fun listHanfuImageByHanfu(hanfuId: Long): Flow<List<HanfuImage>>

    @Query("DELETE FROM hanfu_image WHERE hanfuId = :hanfuId")
    fun deleteHanfuImageByHanfu(hanfuId: Long)

    //**********  hanfu image  ***********//
    @Insert
    suspend fun insertHanfuImage(hanfuImage: HanfuImage)

    @Update
    suspend fun updateHanfuImage(hanfuImage: HanfuImage)

    @Query("DELETE FROM hanfu_image WHERE id = :id")
    suspend fun deleteHanfuImageById(id: Long)

    @Query("""
        SELECT * FROM hanfu_image
        ORDER BY id DESC
    """)
    fun listHanfuImage(): Flow<List<HanfuImage>>





    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJewelry(jewelry: Jewelry): Long

    @Update
    suspend fun updateJewelry(jewelry: Jewelry)

    @Query("DELETE FROM jewelry WHERE id = :id")
    suspend fun deleteJewelryById(id: Long)

    @Query("""
        SELECT * FROM jewelry
        WHERE cabinetId = :cabinetId
        ORDER BY id DESC
    """)
    fun listJewelry(cabinetId: Long): Flow<List<Jewelry>>

    @Query("""
        SELECT cabinetId, COUNT(id) as count
        FROM jewelry
        GROUP BY cabinetId
    """)
    fun countJewelryCabinet(): Flow<List<StatisticsVO>>

    //**********  hanfu cabinet  **********//
    @Insert
    suspend fun insertJewelryCabinet(jewelryCabinet: JewelryCabinet)

    @Update
    suspend fun updateJewelryCabinet(jewelryCabinet: JewelryCabinet)

    @Query("DELETE FROM jewelry_cabinet WHERE id = :id")
    suspend fun deleteJewelryCabinetById(id: Long)

    @Query("""
        SELECT * FROM jewelry_cabinet
        ORDER BY id DESC
    """)
    fun listJewelryCabinet(): Flow<List<JewelryCabinet>>


    @Query("""
        SELECT * FROM jewelry_image
        WHERE jewelryId IN ( SELECT id FROM jewelry WHERE cabinetId = :cabinetId )
        ORDER BY id DESC
    """)
    fun listJewelryImageByCabinet(cabinetId: Long): List<JewelryImage>

    @Query("DELETE FROM jewelry_image WHERE jewelryId IN (SELECT id FROM jewelry WHERE cabinetId = :cabinetId)")
    fun deleteJewelryImageByCabinet(cabinetId: Long)

    @Query("DELETE FROM jewelry WHERE cabinetId = :cabinetId")
    fun deleteJewelryByCabinet(cabinetId: Long)

    @Query("""
        SELECT * FROM jewelry_image
        WHERE jewelryId = :jewelryId
        ORDER BY id DESC
    """)
    fun listJewelryImageByJewelry(jewelryId: Long): Flow<List<JewelryImage>>

    @Query("DELETE FROM jewelry_image WHERE jewelryId = :jewelryId")
    fun deleteJewelryImageByJewelry(jewelryId: Long)

    //**********  hanfu image  ***********//
    @Insert
    suspend fun insertJewelryImage(jewelryImage: JewelryImage)

    @Update
    suspend fun updateJewelryImage(jewelryImage: JewelryImage)

    @Query("DELETE FROM jewelry_image WHERE id = :id")
    suspend fun deleteJewelryImageById(id: Long)

    @Query("""
        SELECT * FROM jewelry_image
        ORDER BY id DESC
    """)
    fun listJewelryImage(): Flow<List<JewelryImage>>



    //**********  Outfit  **********//
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOutfit(outfit: Outfit): Long

    @Update
    suspend fun updateOutfit(outfit: Outfit)

    @Query("DELETE FROM outfit WHERE id = :id")
    suspend fun deleteOutfitById(id: Long)

    @Query("""
        SELECT * FROM outfit
        ORDER BY id DESC
    """)
    fun listOutfit(): Flow<List<Outfit>>

    //**********  outfit image  ***********//
    @Insert
    suspend fun insertOutfitImage(outfitImage: OutfitImage)

    @Update
    suspend fun updateOutfitImage(outfitImage: OutfitImage)

    @Query("DELETE FROM outfit_image WHERE id = :id")
    suspend fun deleteOutfitImageById(id: Long)

    @Query("""
        SELECT * FROM outfit_image
        ORDER BY id DESC
    """)
    fun listOutfitImage(): Flow<List<OutfitImage>>

    @Query("""
        SELECT * FROM outfit_image
        WHERE outfitId = :outfitId
        ORDER BY id DESC
    """)
    fun listOutfitImageByOutfit(outfitId: Long): Flow<List<OutfitImage>>

    @Query("DELETE FROM outfit_image WHERE outfitId = :outfitId")
    fun deleteOutfitImageByOutfit(outfitId: Long)

    @Query("""
        SELECT j.* FROM outfit_hanfu_rel r INNER JOIN hanfu j ON r.relId = j.id
        WHERE r.outfitId = :outfitId AND r.type = 'hanfu'
        ORDER BY j.id DESC
    """)
    fun listHanfuByOutfit(outfitId: Long): Flow<List<Hanfu>>

    @Query("""
        SELECT j.* FROM outfit_hanfu_rel r INNER JOIN jewelry j ON r.relId = j.id
        WHERE r.outfitId = :outfitId AND r.type = 'jewelry'
        ORDER BY j.id DESC
    """)
    fun listJewelryByOutfit(outfitId: Long): Flow<List<Jewelry>>

    @Query("""
        SELECT * FROM hanfu
        ORDER BY id DESC
    """)
    fun listAllHanfu(): Flow<List<Hanfu>>

    @Query("""
        SELECT * FROM jewelry
        ORDER BY id DESC
    """)
    fun listAllJewelry(): Flow<List<Jewelry>>

    @Query("DELETE FROM outfit_hanfu_rel WHERE outfitId = :outfitId")
    fun deleteOutfitByOutfit(outfitId: Long)

    @Insert
    fun insertOutfitHanfuRel(outfitHanfuRel: OutfitHanfuRel)

    @Query("SELECT * FROM hanfu_cabinet WHERE id = :id")
    fun getHanfuCabinetById(id: Long): Flow<HanfuCabinet>

    @Query("SELECT * FROM jewelry_cabinet WHERE id = :id")
    fun getJewelryCabinetById(id: Long): Flow<JewelryCabinet>

}