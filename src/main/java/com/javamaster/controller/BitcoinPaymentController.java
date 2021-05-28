package com.javamaster.controller;

import com.javamaster.controller.dto.PaymentDTO;
import com.javamaster.model.BitcoinPayment;;
import com.javamaster.model.Payment;
import com.javamaster.service.BitcoinPaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/")
public class BitcoinPaymentController {

    private final BitcoinPaymentService bitcoinPaymentService;

    public BitcoinPaymentController(BitcoinPaymentService bitcoinPaymentService) {
        this.bitcoinPaymentService = bitcoinPaymentService;
    }

    @PostMapping("/verifyRequest")
    public String verifyRequest(@RequestBody PaymentDTO paymentDTO,
                                Model model){
        if(paymentDTO.verifyPayment()) {
            BitcoinPayment payment = new BitcoinPayment(paymentDTO);
            payment = bitcoinPaymentService.save(payment);
            model.addAttribute("payment", payment);
            return "displayAddress";
        }
        return "error";
    }

    @GetMapping("/confirmPayment/{id}")
    public String confirmPayment(@PathVariable("id") Long id, Model model){
        BitcoinPayment payment = bitcoinPaymentService.findById(id);

        // inactive and completed payment jump right to transaction status page
        if((payment.getStatus() == Payment.Status.INACTIVE) || (payment.getStatus() == Payment.Status.COMPLETED)) {
            model.addAttribute("payment", payment);
            return "transactionStatus";
        }

        // initiated payments start watching process and change status to pending
        if(payment.getStatus() == Payment.Status.INITIATED) {
            bitcoinPaymentService.watchPayment(payment);
            payment.setStatus(Payment.Status.PENDING);
            bitcoinPaymentService.save(payment);
        }

        // add payment to model
        model.addAttribute("payment", payment);
        return "transactionStatus";
    }

}
