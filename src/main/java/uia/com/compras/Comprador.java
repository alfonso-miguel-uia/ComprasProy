package uia.com.compras;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Comprador {


    private int clasificacion = 0;
    private int minVendedor = 10;
    private int maxVendedor = 13;

    protected HashMap<Integer, HashMap<Integer, ArrayList<InfoComprasUIA>>> solicitudesOrdenCompraAgrupadosXvendedores = new HashMap<Integer, HashMap<Integer, ArrayList<InfoComprasUIA>>>();
    protected HashMap<Integer, ArrayList<Cotizacion>> cotizaciones = new HashMap<Integer, ArrayList<Cotizacion>>();
    protected HashMap<Integer, Cotizacion> cotizacionesVendedoresSeleccionados = new HashMap<Integer, Cotizacion>();
    protected ArrayList<Vendedor> vendedores = new ArrayList<Vendedor>();


    public Comprador()
    {
        for(int i=this.minVendedor; i<this.maxVendedor; i++) {
            Vendedor newVendedor = new Vendedor(i);
            this.vendedores.add(newVendedor);
        }
    }


    public HashMap<Integer, HashMap<Integer, ArrayList<InfoComprasUIA>>> getSolicitudesOrdenCompraAgrupadosXvendedores() {
        return solicitudesOrdenCompraAgrupadosXvendedores;
    }

    public void setSolicitudesOrdenCompraAgrupadosXvendedores(HashMap<Integer, HashMap<Integer, ArrayList<InfoComprasUIA>>> solicitudesOrdenCompraAgrupadosXvendedores) {
        this.solicitudesOrdenCompraAgrupadosXvendedores = solicitudesOrdenCompraAgrupadosXvendedores;
    }

    public HashMap<Integer, ArrayList<Cotizacion>> getCotizaciones() {
        return cotizaciones;
    }

    public void setCotizaciones(HashMap<Integer, ArrayList<Cotizacion>> cotizaciones) {
        this.cotizaciones = cotizaciones;
    }

    public int getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(int clasificacion) {
        this.clasificacion = clasificacion;
    }


    public void hazSolicitudOrdenCompra(PeticionOrdenCompra miPeticionOC) {
        validaExistencia(miPeticionOC);
    }

    private void validaExistencia(PeticionOrdenCompra miPeticionOC) {
        for (int i = 0; i < miPeticionOC.getItems().size(); i++) {
            validaUso((PeticionOrdenCompra) miPeticionOC.getItems().get(i), i);
        }
    }

    private void validaUso(PeticionOrdenCompra miPeticionOC, int i) {
        switch (i % 3) {
            case 0:
                miPeticionOC.setClasificacion(0);
                break;
            case 1:
                miPeticionOC.setClasificacion(1);
                break;
            case 2:
                miPeticionOC.setClasificacion(2);
                break;
        }
    }

    public SolicitudOrdenCompra buscaVendedor(PeticionOrdenCompra miPeticionOC) {
        //--Por downcasting se crea SolicituOrdenCompra para asignar las peticiones con proveedor
        SolicitudOrdenCompra miSolicitudOC = new SolicitudOrdenCompra();

        for (int i = 0; i < miPeticionOC.getItems().size(); i++) {
            SolicitudOrdenCompra item;
            item = new SolicitudOrdenCompra((PeticionOrdenCompra) miPeticionOC.getItems().get(i));
            if (buscaVendedor((SolicitudOrdenCompra) item, i) >= 0) {
                if (miSolicitudOC.getItems() == null)
                    miSolicitudOC.setItems(new ArrayList<InfoComprasUIA>());
                miSolicitudOC.getItems().add(item);
            }
        }
        if (miSolicitudOC != null)
            return miSolicitudOC;
        else
            return null;

    }

    private int buscaVendedor(SolicitudOrdenCompra solicitudOC, int i) {
        switch (i % 3) {
            case 0:
                solicitudOC.setVendedor(0);
                return i;
            case 1:
                solicitudOC.setVendedor(1);
                return i;
            case 2:
                solicitudOC.setVendedor(2);
                return i;
        }
        return -1;
    }

    public void agrupaVendedores(PeticionOrdenCompra peticionOC) {
        SolicitudOrdenCompra newItem = null;
        int key = 0;
        int keyLista = -1;

        if (solicitudesOrdenCompraAgrupadosXvendedores == null)
            solicitudesOrdenCompraAgrupadosXvendedores = new HashMap<Integer, HashMap<Integer, ArrayList<InfoComprasUIA>>>();

        for (int i = 0; i < peticionOC.getItems().size(); i++) {
            newItem = (SolicitudOrdenCompra) peticionOC.getItems().get(i);
            key = newItem.getVendedor();
            keyLista = i % 3;

            if (solicitudesOrdenCompraAgrupadosXvendedores.containsKey(key)) {
                if (solicitudesOrdenCompraAgrupadosXvendedores.get(key).containsKey(keyLista)) {
                    solicitudesOrdenCompraAgrupadosXvendedores.get(key).get(keyLista).add(newItem);
                } else {
                    ArrayList<InfoComprasUIA> newLista = new ArrayList<InfoComprasUIA>();
                    newLista.add(newItem);
                    HashMap<Integer, ArrayList<InfoComprasUIA>> nodo = new HashMap<Integer, ArrayList<InfoComprasUIA>>();
                    nodo.put(i, newLista);
                    solicitudesOrdenCompraAgrupadosXvendedores.put(key, nodo);
                }
            } else {
                ArrayList<InfoComprasUIA> newLista = new ArrayList<InfoComprasUIA>();
                newLista.add(newItem);
                HashMap<Integer, ArrayList<InfoComprasUIA>> nodo = new HashMap<Integer, ArrayList<InfoComprasUIA>>();
                nodo.put(keyLista, newLista);
                solicitudesOrdenCompraAgrupadosXvendedores.put(key, nodo);
            }
        }
    }

    public HashMap<Integer, ArrayList<Cotizacion>> hazCotizaciones(ArrayList<SolicitudOrdenCompra> misSolicitudesOC, ObjectMapper mapper) throws IOException {
        Cotizacion newItem = null;
        Vendedor newVendedor = null;
        Cotizacion newCotizacion;
        int key = 0;
        int keyLista = -1;


        for (int i = 0; i < misSolicitudesOC.size(); i++)
        {
            ArrayList<Cotizacion> listaCotizaciones = new ArrayList<Cotizacion>();

            for(int j=0; j<vendedores.size(); j++)
            {
                Vendedor vendedor = vendedores.get(j);
                newCotizacion = (Cotizacion) vendedor.cotiza(misSolicitudesOC.get(i), mapper);
                listaCotizaciones.add(newCotizacion);
            }
            this.cotizaciones.put(i, listaCotizaciones);
        }

        return this.cotizaciones;
    }


    public HashMap<Integer, Cotizacion> seleccionaVendedores(HashMap<Integer, ArrayList<Cotizacion>> misCotizaciones, ObjectMapper mapper) throws IOException {
        double monto =  Double.MAX_VALUE;
        double newMonto;
        int iMonto = -1;
        ArrayList<Cotizacion> listaCotizaciones;

        for (Map.Entry<Integer, ArrayList<Cotizacion>> nodo : misCotizaciones.entrySet())
        {
            listaCotizaciones = nodo.getValue();

            for(int i=0; i<listaCotizaciones.size(); i++)
            {
                newMonto = listaCotizaciones.get(i).getTotal();
                if(newMonto < monto) {
                    monto = newMonto;
                    iMonto = i;
                }
            }
            cotizacionesVendedoresSeleccionados.put(nodo.getKey(), listaCotizaciones.get(iMonto));
            mapper.writeValue(new File("C:/TSU-2022/ComprasProy/" + listaCotizaciones.get(iMonto).getName() +"-vendedorSeleccionado-"+listaCotizaciones.get(iMonto).getId() +".json"), Cotizacion.class);
        }

        return this.cotizacionesVendedoresSeleccionados;
    }

}
