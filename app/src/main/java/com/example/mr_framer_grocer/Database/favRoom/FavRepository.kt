package com.example.mr_framer_grocer.Database.favRoom

class FavRepository(private  var iFavDataSource: IFavDataSource): IFavDataSource {

    companion object{
        fun getInstance(iFavDataSource: IFavDataSource): FavRepository {
            val instance = FavRepository(iFavDataSource)
            return instance
        }
    }

    override fun getFavItems(): List<Fav> {
        return iFavDataSource.getFavItems()
    }

    override fun isFav(itemId: Int): List<Fav> {
        return iFavDataSource.isFav(itemId)
    }

    override fun deleteFavItem(fav: Fav) {
        return iFavDataSource.deleteFavItem(fav)
    }

    override fun addToFav(vararg fav: Fav) {
        iFavDataSource.addToFav(*fav)
    }

    override fun delById(itemId: Int) {
        iFavDataSource.delById(itemId)
    }

}