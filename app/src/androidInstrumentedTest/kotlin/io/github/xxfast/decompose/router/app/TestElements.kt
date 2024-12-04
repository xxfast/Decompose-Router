package io.github.xxfast.decompose.router.app

import androidx.compose.ui.test.hasTestTag
import io.github.xxfast.decompose.router.screens.BUTTON_BACK
import io.github.xxfast.decompose.router.screens.BOTTOM_NAV_BAR
import io.github.xxfast.decompose.router.screens.BOTTOM_NAV_PAGES
import io.github.xxfast.decompose.router.screens.BOTTOM_NAV_SLOT
import io.github.xxfast.decompose.router.screens.BOTTOM_NAV_STACK
import io.github.xxfast.decompose.router.screens.BOTTOM_SHEET
import io.github.xxfast.decompose.router.screens.BUTTON_BOTTOM_SHEET
import io.github.xxfast.decompose.router.screens.BUTTON_DIALOG
import io.github.xxfast.decompose.router.screens.BUTTON_FORWARD
import io.github.xxfast.decompose.router.screens.DETAILS
import io.github.xxfast.decompose.router.screens.DIALOG
import io.github.xxfast.decompose.router.screens.FAB_ADD
import io.github.xxfast.decompose.router.screens.LIST_TAG
import io.github.xxfast.decompose.router.screens.PAGER
import io.github.xxfast.decompose.router.screens.TITLE_BAR

internal val backButton = hasTestTag(BUTTON_BACK)
internal val forwardButton = hasTestTag(BUTTON_FORWARD)
internal val bottomNav = hasTestTag(BOTTOM_NAV_BAR)
internal val bottomNavPagesItem = hasTestTag(BOTTOM_NAV_PAGES)
internal val bottomNavSlotItem = hasTestTag(BOTTOM_NAV_SLOT)
internal val bottomNavStackItem = hasTestTag(BOTTOM_NAV_STACK)
internal val bottomSheet = hasTestTag(BOTTOM_SHEET)
internal val buttonBottomSheet = hasTestTag(BUTTON_BOTTOM_SHEET)
internal val buttonDialog = hasTestTag(BUTTON_DIALOG)
internal val details = hasTestTag(DETAILS)
internal val dialog = hasTestTag(DIALOG)
internal val fabAdd = hasTestTag(FAB_ADD)
internal val lazyColumn = hasTestTag(LIST_TAG)
internal val pager = hasTestTag(PAGER)
internal val titleBar = hasTestTag(TITLE_BAR)
