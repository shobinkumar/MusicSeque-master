package com.musicseque.interfaces

import com.musicseque.artist.band.band_model.BandMemberStatusModel

interface RemoveMemberInterface {
    fun sendMemberId(id:String)
    fun addOrRemoveMember(id: String, type: String, data: BandMemberStatusModel, pos: Int)
}