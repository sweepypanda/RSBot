package org.rsbot.script.wrappers;

import org.rsbot.client.RSGroundEntity;
import org.rsbot.client.RSGroundObject;
import org.rsbot.script.methods.MethodContext;
import org.rsbot.script.methods.MethodProvider;

/**
 * Represents an item on a tile.
 *
 * @author Jacmob
 */
public class RSGroundItem extends MethodProvider {

	private RSItem groundItem;
	private RSTile location;

	public RSGroundItem(final MethodContext ctx, final RSTile location, final RSItem groundItem) {
		super(ctx);
		this.location = location;
		this.groundItem = groundItem;
	}

	/**
	 * Gets the top model on the tile of this ground item.
	 *
	 * @return The top model on the tile of this ground item.
	 */
	public RSModel getModel() {
		int x = location.getX() - methods.game.getBaseX();
		int y = location.getY() - methods.game.getBaseY();
		int plane = methods.client.getPlane();
		org.rsbot.client.RSGround rsGround = methods.client.getRSGroundArray()[plane][x][y];

		RSGroundEntity obj = rsGround.getGroundObject();
		if (obj != null) {
			return new RSAnimableModel(methods, ((RSGroundObject) rsGround.getGroundObject()).getModel(), obj);
		}
		return null;
	}

	public boolean doAction(String action) {
		RSModel model = getModel();
		if (model != null) {
			methods.mouse.move(model.getPoint());
			sleep(random(5, 50));
			return methods.menu.doAction(action);
		}
		return methods.tiles.doAction(getLocation(), random(0.45, 0.55), random(0.45, 0.55), 0, action);
	}

	public RSItem getItem() {
		return groundItem;
	}

	public RSTile getLocation() {
		return location;
	}

	public boolean isOnScreen() {
		RSModel model = getModel();
		if (model == null) {
			return methods.calc.tileOnScreen(location);
		} else {
			return methods.calc.pointOnScreen(model.getPoint());
		}
	}

}
