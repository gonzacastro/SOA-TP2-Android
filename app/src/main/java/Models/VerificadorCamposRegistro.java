package Models;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerificadorCamposRegistro {
    private final static boolean CAMPOS_VALIDOS = true;
    private final static boolean CAMPOS_INVALIDOS = false;
    private final static String REG_EX_MAIL = "[\\w\\-.]*@[\\w]*(\\.[a-z]{2,3})+";

    private EditText nombre, apellido, dni, email, contrasenia, comision, grupo;

    public VerificadorCamposRegistro(EditText nombre, EditText apellido, EditText dni, EditText email, EditText contrasenia, EditText comision, EditText grupo){
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.contrasenia = contrasenia;
        this.comision = comision;
        this.grupo = grupo;
    }

    public boolean verificarCampos(){
        int nError = 0;
        Pattern patron = Pattern.compile(REG_EX_MAIL);
        Matcher m = patron.matcher(email.getText().toString());

        if(nombre.getText().toString().isEmpty()){
            nombre.setError("El campo no puede estar vacio");
            nError = 1;
        }
        if(apellido.getText().toString().isEmpty()){
            apellido.setError("Debe ingresar un apellido de forma obligatoria");
            nError = 1;
        }
        if(dni.getText().toString().isEmpty()){
            dni.setError("El campo DNI es obligatorio");
            nError = 1;
        }

        if(email.getText().toString().isEmpty() || !m.find()){
            email.setError("Debe ingresar un email con formato valido");
            nError = 1;
        }
        if(contrasenia.getText().toString().isEmpty() || contrasenia.getText().length() < 8){
            contrasenia.setError("Debe ingresar una clave valida");
            nError = 1;
        }
        if(comision.getText().toString().isEmpty() || (!comision.getText().toString().equals("2900") && (!comision.getText().toString().equals("3900")))){
            comision.setError("Debe ingresar una comision valida");
            nError = 1;
        }
        if(grupo.getText().toString().isEmpty()){
            grupo.setError("el campo no puede ser vacio");
            nError = 1;
        }
        if(nError != 0) {
            return CAMPOS_INVALIDOS;
        }
        return CAMPOS_VALIDOS;
    }
}