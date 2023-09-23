import { Component } from '@angular/core';
import { Company } from "./company"

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  companies: Company[] = [
    new Company(
      "111-222-22-22",
      "ul.Jesionowa 2",
      "First INC.",
      22.22,
      11.22
    )
  ];

  newCompany: Company = new Company("", "", "", 0, 0);

  addCompany() {
    this.companies.push(this.newCompany);
    this.newCompany = new Company("", "", "", 0, 0)
  }
  
  deleteCompany(companyToDelete: Company){
    this.companies = this.companies.filter(company => company !==companyToDelete )
  }

  triggerUpdate(company: Company){
    company.editedCompany = new Company(
      company.taxIdentificationNumber,
      company.address,
      company.name,
      company.healthInsurance,
      company.pensionInsurance
    )
    company.editMode = true
  }

  updateCompany(updatedCompany: Company){
    updatedCompany.taxIdentificationNumber = updatedCompany.editedCompany.taxIdentificationNumber
    updatedCompany.address = updatedCompany.editedCompany.address
    updatedCompany.name = updatedCompany.editedCompany.name
    updatedCompany.healthInsurance = updatedCompany.editedCompany.healthInsurance
    updatedCompany.pensionInsurance = updatedCompany.editedCompany.pensionInsurance

    updatedCompany.editMode = false;
  }

  cancelCompanyUpdate(company: Company){
    company.editMode = false
  }
}
