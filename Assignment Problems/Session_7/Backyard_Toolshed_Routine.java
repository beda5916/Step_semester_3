abstract class GardenTool {

    public abstract String use();
}

class CuttingTool extends GardenTool {

    @Override
    public String use() {
        return superUse() + ", blade sharpened first";
    }

    private String superUse() {
        return "Using the tool in the garden";
    }
}

class Pruner extends CuttingTool {

    @Override
    public String use() {
        return super.use() + ", then trimming branches precisely";
    }
}

public class Backyard_Toolshed_Routine {

    public static void main(String[] args) {

        CuttingTool c = new CuttingTool();
        Pruner p = new Pruner();

        System.out.println(c.use());
        System.out.println(p.use());
    }
}