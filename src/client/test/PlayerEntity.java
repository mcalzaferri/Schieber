package client.test;

/** Erweiterte PlayerEntityklasse für Tests. Sonst nicht benutzen!
 * @author Maurus Calzaferri
 *
 */
public class PlayerEntity extends ch.ntb.jass.common.entities.PlayerEntity{
	@Override
	public String toString() {
		return this.name;
	}
}
