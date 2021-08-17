package bot.Ruediger;

public class RuedigerOhneEnhancer extends IRuediger {

    public RuedigerOhneEnhancer(int forecast) {
        super(forecast);
    }

    @Override
    protected void enhanceMaps() {
        //DO NOTHING
    }
}
