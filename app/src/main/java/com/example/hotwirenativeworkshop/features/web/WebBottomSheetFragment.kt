package com.example.hotwirenativeworkshop.features.web

import dev.hotwire.navigation.destinations.HotwireDestinationDeepLink
import dev.hotwire.navigation.fragments.HotwireWebBottomSheetFragment

@HotwireDestinationDeepLink(uri = "hotwire://fragment/web/modal/sheet")
class WebBottomSheetFragment : HotwireWebBottomSheetFragment()