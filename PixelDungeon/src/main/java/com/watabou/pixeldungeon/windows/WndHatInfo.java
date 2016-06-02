package com.watabou.pixeldungeon.windows;

import com.nyrds.pixeldungeon.items.accessories.Accessory;
import com.nyrds.pixeldungeon.support.Iap;
import com.watabou.noosa.Image;
import com.watabou.noosa.Text;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.SystemRedButton;
import com.watabou.pixeldungeon.ui.TextButton;
import com.watabou.pixeldungeon.ui.Window;

public class WndHatInfo extends Window {

	private static final int WIDTH = 100;
	private static final int HEIGHT = 160;
	private static final int MARGIN = 2;
	private static final int BUTTON_HEIGHT = 16;

	public WndHatInfo(final String accessory, String price ) {
		int yPos = 0;

		final Accessory item = Accessory.getByName(accessory);

		Text tfTitle = PixelScene.createMultiline(item.name(), 11);
		tfTitle.hardlight(TITLE_COLOR);
		tfTitle.maxWidth(WIDTH - MARGIN * 2);
		tfTitle.measure();
		tfTitle.x = (WIDTH - tfTitle.width())/2;
		tfTitle.y = MARGIN;

		yPos += tfTitle.height() + MARGIN;
		add(tfTitle);

		Image hat = item.getImage();
		hat.setPos(0,yPos);
		add(hat);

		Text info = PixelScene.createMultiline(item.desc(), 9 );

		info.hardlight(0xFFFFFF);
		info.x = hat.x + hat.width();
		info.y = hat.y;
		info.maxWidth(WIDTH - (int)hat.width());
		info.measure();

		yPos += info.height() + MARGIN;
		add(info);

		TextButton rb = new SystemRedButton(price) {
			@Override
			protected void onClick() {
				super.onClick();

				if(item.haveIt()) {
					Dungeon.hero.getHeroSprite().wearAccessory(item);
					return;
				}

				Iap.doPurchase(accessory, new Iap.IapCallback() {
					@Override
					public void onPurchaseOk() {
						item.gotIt();
						text("Equip");
					}
				});
			}
		};

		rb.setRect(hat.width(),info.y + info.height() + MARGIN, WIDTH / 2, BUTTON_HEIGHT );

		yPos += BUTTON_HEIGHT + MARGIN;
		add(rb);

		int h = Math.min(HEIGHT - MARGIN, yPos);

		resize( WIDTH,  h);


/*
		int yPos = 0;

		Text tfTitle = PixelScene.createMultiline("Hat", 9);
		tfTitle.hardlight(TITLE_COLOR);
		tfTitle.x = tfTitle.y = MARGIN;
		tfTitle.maxWidth(WIDTH - MARGIN * 2);
		tfTitle.measure();

		yPos += tfTitle.height() + MARGIN;
		add(tfTitle);

		resize( WIDTH,  yPos);
*/
	}
}