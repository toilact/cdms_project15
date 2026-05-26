// ============================================================
// File: TrackingService.java
// Package: com.cdms.service
// Description: Quản lý việc gán shipper, cập nhật hành trình,
//              và theo dõi trạng thái giao nhận.
//  🔧 BÀN GIAO CHO: Nguyễn Thanh Tùng (Developer C - Thành viên 4)
//     chuc nang: B10-B15. (nhiem vu phan ParcelOrderView va ShipperView)
// ============================================================

package com.cdms.service;

import java.util.Scanner;
import java.util.List;
import java.time.LocalDate;

import com.cdms.model.DeliveryOrder;
import com.cdms.model.DeliveryStaff;

public class TrackingService 
{
    private Scanner sc = new Scanner(System.in);

    private List<DeliveryOrder> orders;

    private List<DeliveryStaff> staffs;

    public TrackingService(List<DeliveryOrder> orders, List<DeliveryStaff> staffs) 
    {
        this.orders = orders;
        this.staffs = staffs;
    }

    // ============================================================
    // Tim order theo ID
    // ============================================================

    public DeliveryOrder findOrderById(String id) 
    {

        for (DeliveryOrder o : orders) 
        {

            if (o.getId().equals(id)) 
            {
                return o;
            }
        }

        return null;
    }

    // ============================================================
    // B10 - assignStaff() 'Phân công đơn'
    // ============================================================

    public void assignStaff()
    {

        System.out.print("Enter order ID: ");

        String orderId = sc.nextLine();

        DeliveryOrder o = findOrderById(orderId);

        if (o == null)
        {

            System.out.println("Order not found.");

            return;
        }

        System.out.print("Enter staff ID: ");

        String staffId = sc.nextLine();

        DeliveryStaff staff = null;

        for (DeliveryStaff s : staffs)
        {

            if (s.getId().equals(staffId))
            {

                staff = s;
            }
        }

        if (staff == null)
        {

            System.out.println("Staff not found.");

            return;
        }

        if (!staff.getStatus().equalsIgnoreCase("Active"))
        {

            System.out.println("Staff inactive.");

            return;
        }

        if (o.getStaffId() != null && !o.getStaffId().isEmpty())
        {

            System.out.println("Order already assigned.");

            return;
        }

        o.setStaffId(staff.getId());

        o.setStatus("Assigned");

        System.out.println("Assign staff successfully.");
    }

    // ============================================================
    // B11 - viewDeliveredByStaff()  'Xem đơn đã giao'
    // ============================================================

    public void viewDeliveredByStaff()
    {
        System.out.print("Enter staff ID: ");

        String staffId = sc.nextLine();

        boolean found = false;

        System.out.println("===== DELIVERED ORDERS =====");

        for (DeliveryOrder o : orders) 
        {

            if (staffId.equals(o.getStaffId()) && "Delivered".equalsIgnoreCase(o.getStatus())) 
            {

                System.out.println(o);

                found = true;
            }
        }

        if (!found) //oh, van in ra neu dell co don nao duoc giao thanh cong
        {

            System.out.println("No delivered orders.");
        }
    }

    // ============================================================
    // B12 - updateStatus()   'Cập nhật trạng thái'
    // ============================================================

    public void updateStatus() 
    {

        System.out.print("Enter order ID: ");

        String orderId = sc.nextLine();

        DeliveryOrder o = findOrderById(orderId);

        if (o == null) 
        {

            System.out.println("Order not found.");

            return;
        }

        System.out.print("Enter new status: ");

        String newStatus = sc.nextLine();

        String current = o.getStatus();

        // Pending -> In Transit
        if (current.equalsIgnoreCase("Pending") && newStatus.equalsIgnoreCase("In Transit")) 
        {

            o.setStatus(newStatus);
        }

        // Assigned -> In Transit
        else if (current.equalsIgnoreCase("Assigned") && newStatus.equalsIgnoreCase("In Transit")) 
        {

            o.setStatus(newStatus);
        }

        // In Transit -> Delivered
        else if (current.equalsIgnoreCase("In Transit") && newStatus.equalsIgnoreCase("Delivered")) 
        {

            o.setStatus(newStatus);
        }

        // In Transit -> Failed
        else if (current.equalsIgnoreCase("In Transit") && newStatus.equalsIgnoreCase("Failed")) 
        {

            o.setStatus(newStatus);
        }

        else 
        {

            System.out.println("Invalid status transition.");

            return;
        }

        System.out.println("Status updated successfully.");
    }

    // ============================================================
    // B13 - showInTransit()   'Đơn đang giao'
    // ============================================================

    public void showInTransit() 
    {

        boolean found = false;

        System.out.println("===== ORDERS IN TRANSIT =====");

        for (DeliveryOrder o : orders) 
        {

            if ("In Transit".equalsIgnoreCase(o.getStatus())) 
            {

                System.out.println(o);

                found = true;
            }
        }

        if (!found) //o hop le, van in ra neu dell co don nao dang giao
        {

            System.out.println("No orders in transit.");
        }
    }

    // ============================================================
    // B14 - showFailed()
    // ============================================================

    public void showFailed() 
    {

        boolean found = false;

        System.out.println("===== FAILED ORDERS =====");

        for (DeliveryOrder o : orders) 
        {

            if ("Failed".equalsIgnoreCase(o.getStatus())) 
            {

                System.out.println(o);

                found = true;
            }
        }

        if (!found) 
        {

            System.out.println("No failed orders.");
        }
    }

    // ============================================================
    // Xem đơn được giao cho shipper
    // ============================================================

    public void viewAssignedOrders() 
    {
        System.out.print("Enter staff ID: ");

        String staffId = sc.nextLine();

        boolean found = false;

        System.out.println("===== ASSIGNED ORDERS =====");

        for (DeliveryOrder o : orders) 
        {

            if (staffId.equals(o.getStaffId())) 
            {

                System.out.println(o);

                found = true;
            }
        }

        if (!found) 
        {

            System.out.println("No assigned orders.");
        }
    }


    // ============================================================
    // Cập nhật ngày nhận hàng
    // ============================================================

    public void updatePickupDate() 
    {

        System.out.print("Enter order ID: ");

        String orderId = sc.nextLine();

        DeliveryOrder o = findOrderById(orderId);

        if (o == null) 
        {

            System.out.println("Order not found.");

            return;
        }

        System.out.print("Enter pickup date (YYYY-MM-DD): ");
        
        LocalDate pickupDate = LocalDate.parse(sc.nextLine());

        o.setPickupDate(pickupDate);

        System.out.println("Pickup date updated.");
    }

    // ============================================================
    // Cập nhật ngày giao hàng
    // ============================================================

    public void updateDeliveryDate() 
    {

        System.out.print("Enter order ID: ");
        
        String orderId = sc.nextLine();

        DeliveryOrder o = findOrderById(orderId);

        if (o == null) 
        {

            System.out.println("Order not found.");

            return;
        }

        System.out.print("Enter delivery date (YYYY-MM-DD): ");

        LocalDate deliveryDate = LocalDate.parse(sc.nextLine());

        o.setDeliveryDate(deliveryDate);

        System.out.println("Delivery date updated.");
    }

    // ============================================================
    // B15 - addTrackingNote()
    // ============================================================

    public void addTrackingNote() 
    {

        System.out.print("Enter order ID: ");

        String orderId = sc.nextLine();

        DeliveryOrder o = findOrderById(orderId);

        if (o == null) 
        {

            System.out.println("Order not found.");

            return;
        }

        System.out.print("Enter tracking note: ");

        String note = sc.nextLine();

        o.addNote(note);

        System.out.println("Tracking note added.");

    }
}

