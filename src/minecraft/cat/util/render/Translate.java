package cat.util.render;


import cat.util.RenderUtil;

public class Translate {

    private float x;
    private float y;

    public Translate(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public void interpolate(float targetX, float targetY){
        interpolate(targetX, targetY, 8);
    }
    public void interpolate(float targetX, float targetY, int speed) {
        double deltaX = 0;
        double deltaY = 0;
        if(speed != 0){
            deltaX = (Math.abs(targetX - x) * 0.35f)/(10f / speed);
            deltaY = (Math.abs(targetY - y) * 0.35f)/(10f / speed);
        }
        if(speed == -1){
            x = targetX;
            y = targetY;
            return;
        }
        x = calc(targetX, x, RenderUtil.delta, deltaX);
        y = calc(targetY, y, RenderUtil.delta, deltaY);
    }

    public static float calc(float z, float g, long dt, double sp) {
        float d = g - z;
        if (dt < 1) {
            dt = 1;
        }
        if (dt > 1000) {
            dt = 16;
        }
        double val = Math.max(sp * dt / (1000 / 60F), 0.5);
        if (d > sp) {
            g -= val;
            if (g < z) {
                g = z;
            }
        } else if (d < -sp) {
            g += val;
            if (g > z) {
                g = z;
            }
        } else {
            g = z;
        }
        return g;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

}
