package grubi.arsfutura.model.data;

public class OrientationResource<T> {
    private T resource;

    public OrientationResource(T resource) {
        this.resource = resource;
    }

    public T getResource() {
        return resource;
    }

    public void setResource(T resource) {
        this.resource = resource;
    }
}
