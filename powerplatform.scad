module hole(d, h) {
  cylinder(h=h, r = d/2);
}

module rounded_rectangle(width, height, depth, radius) {
  x = (width / 2) - radius;

  x_left = -x;
  x_right = x;

  y_top = radius;
  y_bottom = y_top + depth;

  linear_extrude(height=height)
    hull() {
    // place 4 circles in the corners, with the given radius
    translate([x_left, y_top, 0])
      circle(r=radius);
    translate([x_right, y_top, 0])
      circle(r=radius);
    translate([x_left, y_bottom, 0])
      circle(r=radius);
    translate([x_right, y_bottom, 0])
      circle(r=radius);
  }
}

module platform(width, height, depth) {
  radius = 10;

  difference() {
    rounded_rectangle(width + 6, height, depth + 3, radius);
    translate([0, 0, 2]) rounded_rectangle(width, height, depth, radius);
  }
}

module connector(width, height, depth) {
  radius = 10;

  difference() {
    rounded_rectangle(width + 6, height, depth, radius);
    translate([0, 0, 2]) rounded_rectangle(width, height, depth, radius);
  }
}

connector_depth = 35;
connector_width = 135;
connector_height = 10;

mount_diameter = 30;
mount_radius = mount_diameter / 2;

pad_height = 5;

platform_width = 343;
platform_height = pad_height*2;
platform_depth = 285;

radius = 10;

difference() {
  hull() {
    translate([0,-mount_diameter-8,0]) hole(mount_diameter, platform_height);
    rounded_rectangle(platform_width + 6, platform_height, platform_depth + 2, radius);
    translate([((platform_width - connector_width)/2),-connector_depth-24, 0]) rounded_rectangle(connector_width + 6, connector_height, connector_depth, radius);
  }
  union() {
    translate([0,-mount_diameter-4,0]) hole(mount_diameter, platform_height+10);
    translate([0, 0, platform_height/2]) rounded_rectangle(platform_width, platform_height, platform_depth, radius);
    translate([((platform_width - connector_width)/2),-connector_depth-24, platform_height/2]) rounded_rectangle(connector_width, connector_height, connector_depth+20, radius);
  }
}
