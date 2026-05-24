document.addEventListener("DOMContentLoaded", function () {
    const elementosNumericos = document.querySelectorAll(".formato-numero");
    
    elementosNumericos.forEach(el => {
        let texto = el.textContent.trim();
        let numero = parseInt(texto, 10);
        
        if (!isNaN(numero) && /^\d+$/.test(texto)) { 
            el.title = numero.toLocaleString('en-US'); 
            
            if (numero >= 1000000) {
                el.textContent = (numero / 1000000).toFixed(1).replace(/\.0$/, '') + 'M';
            } else if (numero >= 100000) {
                el.textContent = (numero / 1000).toFixed(1).replace(/\.0$/, '') + 'K';
            } else {
                el.textContent = numero.toLocaleString('en-US'); 
            }
        }
    });
});
