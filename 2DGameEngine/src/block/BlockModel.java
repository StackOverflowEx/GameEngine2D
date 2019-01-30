package block;

import models.RawModel;
import models.Texture;
import models.TexturedModel;

public class BlockModel extends TexturedModel{
	
	private float value;
	private float hardness;
	
	public BlockModel(Texture texture, RawModel model, float value, float hardness) {
		super(texture, model);
		this.value = value;
		this.hardness = hardness;
	}

	public float getValue() {
		return value;
	}

	public float getHardness() {
		return hardness;
	}	

}
