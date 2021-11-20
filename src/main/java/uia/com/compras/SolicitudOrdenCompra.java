package uia.com.compras;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SolicitudOrdenCompra extends PeticionOrdenCompra
{
    private int proveedor=-1;

    @JsonCreator
    public SolicitudOrdenCompra(@JsonProperty("id")int id, @JsonProperty("name")String name,
                               @JsonProperty("codigo")String codigo, @JsonProperty("unidad")String unidad,
                               @JsonProperty("cantidad")int cantidad, @JsonProperty("proveedor")int proveedor,@JsonProperty("clasificacionProveedor")int clasificacionProveedor)
    {
        super(id, name, codigo, unidad, cantidad);
        this.proveedor = proveedor;
    }

    public SolicitudOrdenCompra(PeticionOrdenCompra info)
    {
        super(info.getId(), info.getName(), info.getCodigo(), info.getUnidad(), info.getCantidad());
        this.setClasificacion(info.getClasificacion());
    }

    public SolicitudOrdenCompra() {

    }

    public int getProveedor() {
        return proveedor;
    }

    public void setProveedor(int proveedor) {
        this.proveedor = proveedor;
    }

}
