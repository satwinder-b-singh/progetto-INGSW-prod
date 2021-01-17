import { Component, OnInit } from '@angular/core';
import {Product} from "../../Model/product";
import {ApiService} from "../../Service/api.service";
import {Router} from "@angular/router";
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'product-d',
  templateUrl: './product-d.component.html',
  styleUrls: ['./product-d.component.css']
})
export class ProductDComponent implements OnInit {
  constructor(private api: ApiService,private _route : ActivatedRoute) { }
  product: Product;
  id:number;
  ngOnInit() {



    this._route.queryParams.subscribe(params => {
      console.log(params["user"]);
      this.id = params["user"];
      console.log(this.id);
    });

    this.api.getProductsById(this.id).subscribe(
      data=>{
        console.log("Prodotto caricato correttamente");

        this.product=data;

      },
      error=> console.log("Eccezione")
    )

  }
}
